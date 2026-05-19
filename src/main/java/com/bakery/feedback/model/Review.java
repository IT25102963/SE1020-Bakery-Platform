package com.bakery.feedback.model;

/**
 * ── OOP CONCEPT: ENCAPSULATION ──────────────────────────────────────────────
 * All fields are private. Access is only through public getters and setters.
 *
 * ── OOP CONCEPT: ABSTRACTION ────────────────────────────────────────────────
 * toFileString() and fromFileString() hide the internal file format detail
 * from the rest of the application.
 *
 * This class represents one customer review stored in reviews.txt.
 * File format (one review per line):
 *   reviewId|customerName|cakeName|rating|comment|datePosted|approved
 */
public class Review {

    // ── Private Fields (Encapsulation) ───────────────────────────────────────
    private String  reviewId;        // e.g.  RV-A1B2C3D4
    private String  customerName;    // e.g.  Amal Perera
    private String  cakeName;        // e.g.  Chocolate Fudge Cake
    private int     rating;          // 1 (Terrible) → 5 (Excellent)
    private String  comment;         // Free-text review body
    private String  datePosted;      // ISO date: 2025-06-01
    private boolean approved;        // false = pending/spam  |  true = visible to public

    // ── Constructors ─────────────────────────────────────────────────────────

    /** Default (no-arg) constructor — required by frameworks */
    public Review() {}

    /** Full constructor — used when creating a new Review object */
    public Review(String reviewId, String customerName, String cakeName,
                  int rating, String comment, String datePosted, boolean approved) {
        this.reviewId     = reviewId;
        this.customerName = customerName;
        this.cakeName     = cakeName;
        this.rating       = rating;
        this.comment      = comment;
        this.datePosted   = datePosted;
        this.approved     = approved;
    }

    // ── Getters & Setters (Encapsulation) ────────────────────────────────────

    public String getReviewId()                    { return reviewId; }
    public void   setReviewId(String reviewId)     { this.reviewId = reviewId; }

    public String getCustomerName()                        { return customerName; }
    public void   setCustomerName(String customerName)     { this.customerName = customerName; }

    public String getCakeName()                    { return cakeName; }
    public void   setCakeName(String cakeName)     { this.cakeName = cakeName; }

    public int  getRating()                { return rating; }
    public void setRating(int rating)      { this.rating = rating; }

    public String getComment()                 { return comment; }
    public void   setComment(String comment)   { this.comment = comment; }

    public String getDatePosted()                      { return datePosted; }
    public void   setDatePosted(String datePosted)     { this.datePosted = datePosted; }

    public boolean isApproved()                    { return approved; }
    public void    setApproved(boolean approved)   { this.approved = approved; }

    // ── Utility: generate ⭐ stars string for templates ───────────────────────
    public String getStars() {
        return "⭐".repeat(Math.max(1, Math.min(5, rating)));
    }

    // ── File Serialization (Abstraction) ─────────────────────────────────────

    /**
     * Converts this Review object into a single pipe-delimited line
     * ready to be written to reviews.txt.
     * Example: RV-A1B2|Amal|Choc Cake|5|Amazing!|2025-06-01|false
     */
    public String toFileString() {
        // Replace any pipe characters inside the comment to avoid corrupting the format
        String safeComment = comment.replace("|", "[PIPE]");
        return reviewId + "|" +
               customerName + "|" +
               cakeName + "|" +
               rating + "|" +
               safeComment + "|" +
               datePosted + "|" +
               approved;
    }

    /**
     * Parses one line from reviews.txt and returns a Review object.
     * Returns null if the line is malformed.
     */
    public static Review fromFileString(String line) {
        if (line == null || line.isBlank()) return null;
        String[] parts = line.split("\\|", 7);   // limit=7 keeps all 7 fields
        if (parts.length < 7) return null;

        try {
            String reviewId     = parts[0].trim();
            String customerName = parts[1].trim();
            String cakeName     = parts[2].trim();
            int    rating       = Integer.parseInt(parts[3].trim());
            String comment      = parts[4].trim().replace("[PIPE]", "|");
            String datePosted   = parts[5].trim();
            boolean approved    = Boolean.parseBoolean(parts[6].trim());

            return new Review(reviewId, customerName, cakeName,
                              rating, comment, datePosted, approved);
        } catch (NumberFormatException e) {
            return null;   // skip malformed line
        }
    }

    // ── toString (for debugging) ──────────────────────────────────────────────
    @Override
    public String toString() {
        return "Review{id='" + reviewId + "', customer='" + customerName +
               "', cake='" + cakeName + "', rating=" + rating +
               ", approved=" + approved + "}";
    }
}
