package com.bakery.feedback.service;

import com.bakery.feedback.model.Review;
import com.bakery.feedback.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * ── OOP CONCEPT: ABSTRACTION (Service Layer) ────────────────────────────────
 * The controller only calls clean, named methods like submitReview().
 * It never touches files or knows HOW data is stored — that's hidden here.
 *
 * ── OOP CONCEPT: DEPENDENCY INJECTION ───────────────────────────────────────
 * ReviewRepository is injected via the constructor (not hard-coded with new).
 * This is a core OOP + Spring principle for loose coupling.
 *
 * Business Logic handled here:
 *  - Auto-generate a unique Review ID (UUID-based)
 *  - Auto-set today's date
 *  - New reviews default to approved=false (pending moderation)
 *  - Validate rating range 1–5
 */
@Service   // Marks this class as a Spring service bean
public class ReviewService {

    // ── Dependency Injection (OOP: loose coupling) ───────────────────────────
    private final ReviewRepository reviewRepository;

    /**
     * Constructor injection — Spring automatically provides the
     * ReviewRepository bean when this Service is created.
     */
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  CREATE — Submit a new customer review
    // ════════════════════════════════════════════════════════════════════════
    /**
     * Builds a Review object with auto-generated ID and today's date,
     * then delegates saving to the repository.
     * All new reviews start as pending (approved=false).
     *
     * @param customerName name of the reviewer
     * @param cakeName     name of the cake being reviewed
     * @param rating       integer 1–5
     * @param comment      free-text review body
     * @throws IOException       if file writing fails
     * @throws IllegalArgumentException if rating is out of range
     */
    public void submitReview(String customerName, String cakeName,
                             int rating, String comment) throws IOException {

        // Business rule: rating must be between 1 and 5
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        // Auto-generate a short unique ID: RV-XXXXXXXX
        String id = "RV-" + UUID.randomUUID()
                                 .toString()
                                 .replace("-", "")
                                 .substring(0, 8)
                                 .toUpperCase();

        // Auto-set today's date in ISO format (yyyy-MM-dd)
        String today = LocalDate.now().toString();

        // New reviews are ALWAYS pending (approved=false) until admin approves
        Review review = new Review(id, customerName, cakeName,
                                   rating, comment, today, false);

        reviewRepository.save(review);   // delegate to file I/O layer
    }

    // ════════════════════════════════════════════════════════════════════════
    //  READ — Get ALL reviews (for admin moderation panel)
    // ════════════════════════════════════════════════════════════════════════
    /**
     * Returns every review regardless of approval status.
     * Used by the Admin Moderation Page.
     *
     * @return list of all reviews
     * @throws IOException if file reading fails
     */
    public List<Review> getAllReviews() throws IOException {
        return reviewRepository.findAll();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  READ — Get only APPROVED reviews (for public testimonials page)
    // ════════════════════════════════════════════════════════════════════════
    /**
     * Returns only reviews that an admin has approved.
     * Customers on the public page cannot see pending/spam reviews.
     *
     * @return list of approved reviews
     * @throws IOException if file reading fails
     */
    public List<Review> getApprovedReviews() throws IOException {
        return reviewRepository.findApproved();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  UPDATE — Approve a pending review
    // ════════════════════════════════════════════════════════════════════════
    /**
     * Marks a review as approved so it appears on the public testimonials page.
     *
     * @param reviewId the ID of the review to approve
     * @return true if found and approved, false otherwise
     * @throws IOException if file reading/writing fails
     */
    public boolean approveReview(String reviewId) throws IOException {
        return reviewRepository.approveById(reviewId);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  DELETE — Remove a spam or inappropriate review
    // ════════════════════════════════════════════════════════════════════════
    /**
     * Permanently deletes a review from reviews.txt.
     * Used by the admin to remove spam or abusive content.
     *
     * @param reviewId the ID of the review to delete
     * @return true if deleted, false if not found
     * @throws IOException if file reading/writing fails
     */
    public boolean deleteReview(String reviewId) throws IOException {
        return reviewRepository.deleteById(reviewId);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  READ — Get one review (for edit screen)
    // ════════════════════════════════════════════════════════════════════════
    public Review getReviewById(String reviewId) throws IOException {
        return reviewRepository.findById(reviewId);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  UPDATE — Edit a review (admin)
    // ════════════════════════════════════════════════════════════════════════
    public boolean updateReview(String reviewId,
                                String customerName,
                                String cakeName,
                                int rating,
                                String comment,
                                boolean approved) throws IOException {

        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        Review existing = reviewRepository.findById(reviewId);
        if (existing == null) return false;

        Review updated = new Review(
                existing.getReviewId(),
                customerName,
                cakeName,
                rating,
                comment,
                existing.getDatePosted(),
                approved
        );

        return reviewRepository.updateById(reviewId, updated);
    }
}
