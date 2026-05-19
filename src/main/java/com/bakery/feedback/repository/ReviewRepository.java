package com.bakery.feedback.repository;

import com.bakery.feedback.model.Review;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * ── OOP CONCEPT: SINGLE RESPONSIBILITY ──────────────────────────────────────
 * This class has ONE job: read and write reviews to reviews.txt.
 * All CRUD file-handling operations live here.
 *
 * ── FILE HANDLING (reviews.txt) ─────────────────────────────────────────────
 * CREATE  →  save()        — appends one new line to reviews.txt
 * READ    →  findAll()     — reads every line into a List<Review>
 * READ    →  findApproved()— filters only approved=true reviews
 * UPDATE  →  approveById() — flips approved flag and rewrites file
 * DELETE  →  deleteById()  — removes a line and rewrites file
 */
@Repository   // OOP: marks this as a Spring-managed bean (Repository layer)
public class ReviewRepository {

    // Injected from application.properties → app.reviews.file=reviews.txt
    @Value("${app.reviews.file}")
    private String filePath;

    // ════════════════════════════════════════════════════════════════════════
    //  CREATE — Append a new review line to reviews.txt
    // ════════════════════════════════════════════════════════════════════════
    /**
     * Writes one review to the end of reviews.txt.
     * Uses append mode (true) so existing reviews are never overwritten.
     *
     * @param review the Review object to persist
     * @throws IOException if the file cannot be written
     */
    public void save(Review review) throws IOException {
        ensureParentDirectory();
        // BufferedWriter with append=true adds to the file instead of replacing it
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(review.toFileString());
            writer.newLine();   // ensures each review is on its own line
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  READ — Load ALL reviews from reviews.txt
    // ════════════════════════════════════════════════════════════════════════
    /**
     * Reads every line of reviews.txt and parses each into a Review object.
     * Returns an empty list (not null) if the file doesn't exist yet.
     *
     * @return List of all Review objects
     * @throws IOException if the file exists but cannot be read
     */
    public List<Review> findAll() throws IOException {
        List<Review> reviews = new ArrayList<>();
        File file = new File(filePath);

        // If no reviews have been submitted yet, the file won't exist — that's fine
        if (!file.exists()) return reviews;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Review r = Review.fromFileString(line);   // parse each line
                if (r != null) reviews.add(r);           // skip malformed lines
            }
        }
        return reviews;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  READ (filtered) — Load only APPROVED reviews for public display
    // ════════════════════════════════════════════════════════════════════════
    /**
     * Returns only reviews where approved=true.
     * Used by the public Testimonials page.
     *
     * @return List of approved Review objects
     * @throws IOException if the file cannot be read
     */
    public List<Review> findApproved() throws IOException {
        List<Review> approved = new ArrayList<>();
        for (Review r : findAll()) {
            if (r.isApproved()) approved.add(r);
        }
        return approved;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  UPDATE — Approve a pending review (flip approved = true)
    // ════════════════════════════════════════════════════════════════════════
    /**
     * Finds a review by ID, sets its approved flag to true,
     * then rewrites the entire file with the updated data.
     *
     * @param reviewId the unique ID of the review to approve
     * @return true if found and updated, false if ID not found
     * @throws IOException if the file cannot be read or written
     */
    public boolean approveById(String reviewId) throws IOException {
        List<Review> all = findAll();
        boolean found = false;

        for (Review r : all) {
            if (r.getReviewId().equals(reviewId)) {
                r.setApproved(true);   // UPDATE the field
                found = true;
                break;
            }
        }

        if (found) rewriteFile(all);   // persist the change
        return found;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  DELETE — Remove a review (spam / moderation)
    // ════════════════════════════════════════════════════════════════════════
    /**
     * Removes the review with the given ID from the list,
     * then rewrites the file without it.
     *
     * @param reviewId the unique ID of the review to delete
     * @return true if deleted, false if ID not found
     * @throws IOException if the file cannot be read or written
     */
    public boolean deleteById(String reviewId) throws IOException {
        List<Review> all = findAll();
        // removeIf returns true if at least one element was removed
        boolean removed = all.removeIf(r -> r.getReviewId().equals(reviewId));
        if (removed) rewriteFile(all);
        return removed;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  READ (by id) — Find a single review
    // ════════════════════════════════════════════════════════════════════════
    public Review findById(String reviewId) throws IOException {
        for (Review r : findAll()) {
            if (r.getReviewId().equals(reviewId)) {
                return r;
            }
        }
        return null;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  UPDATE (by id) — Update review fields and rewrite file
    // ════════════════════════════════════════════════════════════════════════
    public boolean updateById(String reviewId, Review updated) throws IOException {
        List<Review> all = findAll();
        boolean found = false;

        for (int i = 0; i < all.size(); i++) {
            Review existing = all.get(i);
            if (existing.getReviewId().equals(reviewId)) {
                all.set(i, updated);
                found = true;
                break;
            }
        }

        if (found) rewriteFile(all);
        return found;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  PRIVATE HELPER — Overwrite reviews.txt with a fresh list
    // ════════════════════════════════════════════════════════════════════════
    /**
     * Replaces the entire contents of reviews.txt with the given list.
     * Called after UPDATE and DELETE operations.
     * Uses append=false (overwrite mode).
     *
     * @param reviews the complete, updated list of reviews to write
     * @throws IOException if the file cannot be written
     */
    private void rewriteFile(List<Review> reviews) throws IOException {
        ensureParentDirectory();
        // append=false → this creates a fresh file (overwrites old content)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (Review r : reviews) {
                writer.write(r.toFileString());
                writer.newLine();
            }
        }
    }

    private void ensureParentDirectory() throws IOException {
        Path target = Path.of(filePath).toAbsolutePath().normalize();
        Path parent = target.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }
}
