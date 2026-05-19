package com.bakery.feedback.controller;

import com.bakery.feedback.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ── OOP CONCEPT: SEPARATION OF CONCERNS ─────────────────────────────────────
 * The controller only handles HTTP input/output.
 * It does NOT touch files or build business logic — that's in ReviewService.
 *
 * ── ROUTES EXPOSED ──────────────────────────────────────────────────────────
 *  GET  /reviews/new               → UI 1: Show Submit Review Form
 *  POST /reviews/new               → UI 1: Process form submission (CREATE)
 *  GET  /reviews/testimonials      → UI 2: Public Testimonials Page  (READ)
 *  GET  /reviews/admin             → UI 3: Admin Moderation Panel    (READ)
 *  POST /reviews/admin/approve/{id}→ UI 3: Approve a review          (UPDATE)
 *  GET  /reviews/admin/edit/{id}   → UI 3: Edit a review (form)      (UPDATE)
 *  POST /reviews/admin/edit/{id}   → UI 3: Save edited review        (UPDATE)
 *  POST /reviews/admin/delete/{id} → UI 3: Delete a review           (DELETE)
 */
@Controller                    // Returns HTML views (not raw JSON)
@RequestMapping("/reviews")    // All routes in this class start with /reviews
public class  ReviewController {

    // ── Dependency Injection ─────────────────────────────────────────────────
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Root redirect → /reviews/new
    // ════════════════════════════════════════════════════════════════════════
    @GetMapping("/")
    public String root() {
        return "redirect:/reviews/new";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  UI 1 ── Submit Review Form (GET — display the empty form)
    // ════════════════════════════════════════════════════════════════════════
    /**
     * Shows the blank review submission form.
     * Renders: templates/submit-review.html
     */
    @GetMapping("/submit")
    public String showSubmitForm(Model model) {
        return "redirect:/reviews/new";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  UI 1 ── New Review (GET — show submission form)
    // ════════════════════════════════════════════════════════════════════════
    @GetMapping("/new")
    public String showNewReviewForm() {
        return "submit-review";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  UI 1 ── Manage Reviews (GET — list + edit/delete)
    // ════════════════════════════════════════════════════════════════════════
    @GetMapping("/manage")
    public String showManageReviews(Model model) {
        try {
            model.addAttribute("reviews", reviewService.getAllReviews());
        } catch (Exception e) {
            model.addAttribute("errorMsg", "Could not load reviews: " + e.getMessage());
        }
        return "manage-reviews";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  UI 1 ── Customer: Edit Review (GET — show form)
    // ════════════════════════════════════════════════════════════════════════
    @GetMapping("/edit/{id}")
    public String showCustomerEditForm(@PathVariable("id") String reviewId,
                                       Model model,
                                       RedirectAttributes redirectAttributes) {
        try {
            var review = reviewService.getReviewById(reviewId);
            if (review == null) {
                redirectAttributes.addFlashAttribute("errorMsg", "⚠️ Review not found: " + reviewId);
                return "redirect:/reviews/manage";
            }
            model.addAttribute("review", review);
            return "edit-review-customer";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "❌ Could not load review: " + e.getMessage());
            return "redirect:/reviews/manage";
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  UI 1 ── Customer: Edit Review (POST — save changes)
    // ════════════════════════════════════════════════════════════════════════
    @PostMapping("/edit/{id}")
    public String saveCustomerEdit(@PathVariable("id") String reviewId,
                                   @RequestParam("customerName") String customerName,
                                   @RequestParam("cakeName") String cakeName,
                                   @RequestParam("rating") int rating,
                                   @RequestParam("comment") String comment,
                                   RedirectAttributes redirectAttributes) {
        try {
            var existing = reviewService.getReviewById(reviewId);
            if (existing == null) {
                redirectAttributes.addFlashAttribute("errorMsg", "⚠️ Review not found: " + reviewId);
                return "redirect:/reviews/manage";
            }

            boolean success = reviewService.updateReview(
                    reviewId,
                    customerName,
                    cakeName,
                    rating,
                    comment,
                    existing.isApproved()
            );

            if (success) {
                redirectAttributes.addFlashAttribute("successMsg", "✅ Review updated: " + reviewId);
            } else {
                redirectAttributes.addFlashAttribute("errorMsg", "⚠️ Review not found: " + reviewId);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "❌ Error: " + e.getMessage());
        }

        return "redirect:/reviews/manage";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  UI 1 ── Customer: Delete Review (POST)
    // ════════════════════════════════════════════════════════════════════════
    @PostMapping("/delete/{id}")
    public String deleteCustomerReview(@PathVariable("id") String reviewId,
                                       RedirectAttributes redirectAttributes) {
        try {
            boolean success = reviewService.deleteReview(reviewId);
            if (success) {
                redirectAttributes.addFlashAttribute("successMsg", "🗑️ Review deleted: " + reviewId);
            } else {
                redirectAttributes.addFlashAttribute("errorMsg", "⚠️ Review not found: " + reviewId);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "❌ Error: " + e.getMessage());
        }

        return "redirect:/reviews/manage";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  UI 1 ── Submit Review Form (POST — CREATE a new review)
    // ════════════════════════════════════════════════════════════════════════
    /**
     * Receives form data, calls the service to save the review,
     * then redirects back to the form with a success or error flash message.
     *
     * @RequestParam maps each HTML <input name="..."> to a Java parameter.
     */
    @PostMapping("/new")
    public String submitReview(
            @RequestParam("customerName") String customerName,
            @RequestParam("cakeName")     String cakeName,
            @RequestParam("rating")       int    rating,
            @RequestParam("comment")      String comment,
            RedirectAttributes            redirectAttributes) {

        try {
            reviewService.submitReview(customerName, cakeName, rating, comment);
            redirectAttributes.addFlashAttribute("successMsg",
                "✅ Thank you " + customerName + "! Your review has been submitted and is pending approval.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg",
                "❌ Error submitting review: " + e.getMessage());
        }

        // PRG pattern (Post → Redirect → Get) prevents duplicate submissions on refresh
        return "redirect:/reviews/manage";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  UI 2 ── Public Testimonials Page (GET — READ approved reviews)
    // ════════════════════════════════════════════════════════════════════════
    /**
     * Loads only approved reviews and passes them to the Thymeleaf template.
     * Renders: templates/testimonials.html
     *
     * @param model  Spring's model object — used to pass data to the HTML template
     */
    @GetMapping("/testimonials")
    public String showTestimonials(Model model) {
        try {
            var reviews = reviewService.getApprovedReviews();
            double averageRating = reviews.stream()
                    .mapToInt(r -> r.getRating())
                    .average()
                    .orElse(0.0);

            model.addAttribute("reviews", reviews);
            model.addAttribute("averageRating", reviews.isEmpty() ? null : averageRating);
        } catch (Exception e) {
            model.addAttribute("errorMsg", "Could not load reviews: " + e.getMessage());
        }
        return "testimonials";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  UI 3 ── Admin Moderation Panel (GET — READ all reviews)
    // ════════════════════════════════════════════════════════════════════════
    /**
     * Loads ALL reviews (approved + pending) for the admin to manage.
     * Renders: templates/admin-moderation.html
     */
    @GetMapping("/admin")
    public String showAdminPanel(Model model) {
        try {
            model.addAttribute("reviews", reviewService.getAllReviews());
        } catch (Exception e) {
            model.addAttribute("errorMsg", "Could not load reviews: " + e.getMessage());
        }
        return "admin-moderation";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  UI 3 ── Admin: Edit Review (GET — show form)
    // ════════════════════════════════════════════════════════════════════════
    @GetMapping("/admin/edit/{id}")
    public String showEditForm(@PathVariable("id") String reviewId,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            var review = reviewService.getReviewById(reviewId);
            if (review == null) {
                redirectAttributes.addFlashAttribute("errorMsg", "⚠️ Review not found: " + reviewId);
                return "redirect:/reviews/admin";
            }
            model.addAttribute("review", review);
            return "edit-review";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "❌ Could not load review: " + e.getMessage());
            return "redirect:/reviews/admin";
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  UI 3 ── Admin: Edit Review (POST — save changes)
    // ════════════════════════════════════════════════════════════════════════
    @PostMapping("/admin/edit/{id}")
    public String saveEdit(
            @PathVariable("id") String reviewId,
            @RequestParam("customerName") String customerName,
            @RequestParam("cakeName") String cakeName,
            @RequestParam("rating") int rating,
            @RequestParam("comment") String comment,
            @RequestParam(value = "approved", required = false) String approved,
            RedirectAttributes redirectAttributes) {

        boolean approvedBool = approved != null;

        try {
            boolean success = reviewService.updateReview(
                    reviewId, customerName, cakeName, rating, comment, approvedBool
            );
            if (success) {
                redirectAttributes.addFlashAttribute("successMsg", "✅ Review updated: " + reviewId);
            } else {
                redirectAttributes.addFlashAttribute("errorMsg", "⚠️ Review not found: " + reviewId);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "❌ Error: " + e.getMessage());
        }

        return "redirect:/reviews/admin";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  UI 3 ── Admin: Approve a Review (POST — UPDATE)
    // ════════════════════════════════════════════════════════════════════════
    /**
     * Approves a pending review so it becomes visible on the public page.
     *
     * @PathVariable  {id}  is taken from the URL: /reviews/admin/approve/RV-XXXX
     */
    @PostMapping("/admin/approve/{id}")
    public String approveReview(
            @PathVariable("id") String reviewId,
            RedirectAttributes  redirectAttributes) {

        try {
            boolean success = reviewService.approveReview(reviewId);
            if (success) {
                redirectAttributes.addFlashAttribute("successMsg",
                    "✅ Review " + reviewId + " has been approved and is now public.");
            } else {
                redirectAttributes.addFlashAttribute("errorMsg",
                    "⚠️ Review " + reviewId + " was not found.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "❌ Error: " + e.getMessage());
        }

        return "redirect:/reviews/admin";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  UI 3 ── Admin: Delete a Review (POST — DELETE spam)
    // ════════════════════════════════════════════════════════════════════════
    /**
     * Permanently deletes a review (spam / inappropriate content).
     *
     * @PathVariable  {id}  is taken from the URL: /reviews/admin/delete/RV-XXXX
     */
    @PostMapping("/admin/delete/{id}")
    public String deleteReview(
            @PathVariable("id") String reviewId,
            RedirectAttributes  redirectAttributes) {

        try {
            boolean success = reviewService.deleteReview(reviewId);
            if (success) {
                redirectAttributes.addFlashAttribute("successMsg",
                    "🗑️ Review " + reviewId + " has been permanently deleted.");
            } else {
                redirectAttributes.addFlashAttribute("errorMsg",
                    "⚠️ Review " + reviewId + " was not found.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "❌ Error: " + e.getMessage());
        }

        return "redirect:/reviews/admin";
    }
}
