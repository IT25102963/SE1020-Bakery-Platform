package lk.sliit.it25.bakeryweb.controller;

import com.bakery.feedback.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import lk.sliit.it25.bakeryweb.model.Cake;
import lk.sliit.it25.bakeryweb.repository.CatalogRepository;
import lk.sliit.it25.bakeryweb.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CatalogController {

    private final CatalogRepository catalogRepository;
    private final CartService cartService;
    private final ReviewService reviewService;

    public CatalogController(CatalogRepository catalogRepository, CartService cartService, ReviewService reviewService) {
        this.catalogRepository = catalogRepository;
        this.cartService = cartService;
        this.reviewService = reviewService;
    }

    @GetMapping({"/", "/catalog"})
    public String showCatalog(Model model, HttpSession session) {
        List<Cake> cakes = catalogRepository.findAll();
        model.addAttribute("cakes", cakes);
        model.addAttribute("cartCount", cartService.getCartCount(session));
        try {
            var allReviews = reviewService.getAllReviews();
            int reviewCount = allReviews.size();
            double averageRating = allReviews.stream()
                    .mapToInt(r -> Math.max(1, Math.min(5, r.getRating())))
                    .average()
                    .orElse(0.0);

            int[] starCounts = new int[6];
            for (var review : allReviews) {
                int rating = Math.max(1, Math.min(5, review.getRating()));
                starCounts[rating]++;
            }

            model.addAttribute("approvedReviews", allReviews);
            model.addAttribute("reviewCount", reviewCount);
            model.addAttribute("averageRating", averageRating);
            model.addAttribute("star5Pct", toPercentage(starCounts[5], reviewCount));
            model.addAttribute("star4Pct", toPercentage(starCounts[4], reviewCount));
            model.addAttribute("star3Pct", toPercentage(starCounts[3], reviewCount));
            model.addAttribute("star2Pct", toPercentage(starCounts[2], reviewCount));
            model.addAttribute("star1Pct", toPercentage(starCounts[1], reviewCount));
            model.addAttribute("reviewsVersion", buildReviewsVersion(allReviews));
        } catch (IOException e) {
            model.addAttribute("approvedReviews", List.of());
            model.addAttribute("reviewCount", 0);
            model.addAttribute("averageRating", 0.0);
            model.addAttribute("star5Pct", 0);
            model.addAttribute("star4Pct", 0);
            model.addAttribute("star3Pct", 0);
            model.addAttribute("star2Pct", 0);
            model.addAttribute("star1Pct", 0);
            model.addAttribute("reviewsVersion", "0");
        }
        return "catalog/catalog";
    }

    @GetMapping("/catalog/reviews/version")
    @ResponseBody
    public Map<String, Object> reviewsVersion() {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            var reviews = reviewService.getAllReviews();
            response.put("version", buildReviewsVersion(reviews));
            response.put("count", reviews.size());
        } catch (IOException e) {
            response.put("version", "0");
            response.put("count", 0);
        }
        return response;
    }

    private int toPercentage(int count, int total) {
        if (total <= 0) {
            return 0;
        }
        return (int) Math.round((count * 100.0) / total);
    }

    private String buildReviewsVersion(List<com.bakery.feedback.model.Review> reviews) {
        StringBuilder key = new StringBuilder();
        for (var review : reviews) {
            key.append(review.getReviewId() == null ? "" : review.getReviewId().trim()).append('|')
                    .append(review.getRating()).append('|')
                    .append(review.isApproved()).append('|')
                    .append(review.getComment() == null ? "" : review.getComment().trim()).append(';');
        }
        return Integer.toHexString(key.toString().hashCode());
    }
}
