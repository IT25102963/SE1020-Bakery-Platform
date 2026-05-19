package lk.sliit.it25.bakeryweb.customrequests;

import jakarta.servlet.http.HttpSession;
import lk.sliit.it25.bakeryweb.model.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/custom-requests")
public class CustomRequestController {


    private static final String FILE_PATH = "data/custom_requests.txt";

    @GetMapping
    public String viewRequests(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        boolean isAdmin = isAdminSession(session);
        List<CustomRequest> list = readRequestsFromFile();
        if (!isAdmin) {
            Customer customer = getLoggedInCustomer(session);
            list = filterRequestsForCustomer(list, customer);
            model.addAttribute("customerName", customer != null ? customer.getName() : "");
        }

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("requests", list);
        return "customrequests/view";
    }


    @GetMapping("/submit")
    public String showSubmitForm(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (isAdminSession(session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Admins cannot submit customer requests.");
            return "redirect:/custom-requests";
        }

        Customer customer = getLoggedInCustomer(session);
        if (customer == null) {
            return "redirect:/login";
        }

        model.addAttribute("customerName", customer.getName());
        return "customrequests/submit";
    }


    @PostMapping("/submit")
    public String submitRequest(@RequestParam String requestId,
                                @RequestParam String tiers,
                                @RequestParam String theme,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        if (isAdminSession(session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Admins cannot submit customer requests.");
            return "redirect:/custom-requests";
        }

        Customer customer = getLoggedInCustomer(session);
        if (customer == null) {
            return "redirect:/login";
        }

        String record = safeTrim(requestId) + "," + safeTrim(customer.getName()) + "," + safeTrim(tiers) + ","
                + safeTrim(theme) + ",Pending\n";
        try {
            File file = new File(FILE_PATH);
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        redirectAttributes.addFlashAttribute("successMessage", "Custom request submitted successfully.");
        return "redirect:/custom-requests";
    }


    @GetMapping("/edit")
    public String showEditForm(@RequestParam String id, Model model, HttpSession session,
                               RedirectAttributes redirectAttributes) {
        if (!isAdminSession(session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Only admins can update request status.");
            return "redirect:/custom-requests";
        }

        List<CustomRequest> list = readRequestsFromFile();
        for (CustomRequest req : list) {
            if (req.getRequestId().equals(id)) {
                model.addAttribute("request", req);
                return "customrequests/edit";
            }
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Request not found.");
        return "redirect:/custom-requests";
    }

    @GetMapping("/details")
    public String showRequestDetails(@RequestParam String id, Model model, HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        boolean isAdmin = isAdminSession(session);
        List<CustomRequest> list = readRequestsFromFile();
        for (CustomRequest req : list) {
            if (!safeTrim(req.getRequestId()).equalsIgnoreCase(safeTrim(id))) {
                continue;
            }

            if (!isAdmin) {
                Customer customer = getLoggedInCustomer(session);
                if (customer == null || !safeTrim(req.getCustomerName()).equalsIgnoreCase(safeTrim(customer.getName()))) {
                    redirectAttributes.addFlashAttribute("errorMessage", "You cannot access this request.");
                    return "redirect:/custom-requests";
                }
            }

            model.addAttribute("request", req);
            model.addAttribute("isAdmin", isAdmin);
            return "customrequests/details";
        }

        redirectAttributes.addFlashAttribute("errorMessage", "Request not found.");
        return "redirect:/custom-requests";
    }


    @PostMapping("/edit")
    public String editRequest(@RequestParam String requestId,
                              @RequestParam String status,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        if (!isAdminSession(session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Only admins can update request status.");
            return "redirect:/custom-requests";
        }

        String normalizedStatus = normalizeRequestStatus(status);
        if (normalizedStatus == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid request status.");
            return "redirect:/custom-requests";
        }

        List<CustomRequest> list = readRequestsFromFile();
        boolean updated = false;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (CustomRequest req : list) {
                if (req.getRequestId().equals(requestId)) {
                    updated = true;
                    writer.write(req.getRequestId() + "," + req.getCustomerName() + "," + req.getTiers() + ","
                            + req.getTheme() + "," + normalizedStatus + "\n");
                } else {

                    writer.write(req.getRequestId() + "," + req.getCustomerName() + "," + req.getTiers() + "," + req.getTheme() + "," + req.getStatus() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update request status.");
            return "redirect:/custom-requests";
        }

        if (!updated) {
            redirectAttributes.addFlashAttribute("errorMessage", "Request not found.");
            return "redirect:/custom-requests";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Request updated successfully.");
        return "redirect:/custom-requests";
    }


    @GetMapping("/delete")
    public String deleteRequest(@RequestParam String id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdminSession(session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Only admins can delete requests.");
            return "redirect:/custom-requests";
        }

        List<CustomRequest> list = readRequestsFromFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (CustomRequest req : list) {

                if (!req.getRequestId().equals(id)) {
                    writer.write(req.getRequestId() + "," + req.getCustomerName() + "," + req.getTiers() + "," + req.getTheme() + "," + req.getStatus() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        redirectAttributes.addFlashAttribute("successMessage", "Request deleted successfully.");
        return "redirect:/custom-requests";
    }

    @GetMapping("/request-delete")
    public String requestDelete(@RequestParam String id, HttpSession session, RedirectAttributes redirectAttributes) {
        Customer customer = getLoggedInCustomer(session);
        if (customer == null) {
            return "redirect:/login";
        }

        List<CustomRequest> list = readRequestsFromFile();
        boolean found = false;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (CustomRequest req : list) {
                String reqId = safeTrim(req.getRequestId());
                String reqCustomer = safeTrim(req.getCustomerName());
                if (!reqId.equalsIgnoreCase(safeTrim(id)) || !reqCustomer.equalsIgnoreCase(safeTrim(customer.getName()))) {
                    writer.write(req.getRequestId() + "," + req.getCustomerName() + "," + req.getTiers() + "," + req.getTheme() + "," + req.getStatus() + "\n");
                    continue;
                }

                found = true;
                String normalizedStatus = normalizeRequestStatus(req.getStatus());
                if ("Pending".equalsIgnoreCase(normalizedStatus)) {
                    continue;
                }
                writer.write(req.getRequestId() + "," + req.getCustomerName() + "," + req.getTiers() + "," + req.getTheme() + ",Delete Requested\n");
            }
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to process delete request.");
            return "redirect:/custom-requests";
        }

        if (!found) {
            redirectAttributes.addFlashAttribute("errorMessage", "Request not found.");
            return "redirect:/custom-requests";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Delete request processed.");
        return "redirect:/custom-requests";
    }


    private List<CustomRequest> readRequestsFromFile() {
        List<CustomRequest> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    list.add(new CustomRequest(data[0], data[1], data[2], data[3], data[4]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<CustomRequest> filterRequestsForCustomer(List<CustomRequest> requests, Customer customer) {
        if (customer == null) {
            return List.of();
        }

        String customerName = safeTrim(customer.getName());
        return requests.stream()
                .filter(req -> customerName.equalsIgnoreCase(safeTrim(req.getCustomerName())))
                .collect(Collectors.toList());
    }

    private String normalizeRequestStatus(String status) {
        if (status == null) {
            return null;
        }

        return switch (safeTrim(status).toLowerCase(Locale.ROOT)) {
            case "pending" -> "Pending";
            case "confirmed", "approved" -> "Confirmed";
            case "completed", "complete" -> "Completed";
            case "delete requested", "delete-requested", "delete_requested" -> "Delete Requested";
            default -> null;
        };
    }

    private boolean isLoggedIn(HttpSession session) {
        return isAdminSession(session) || getLoggedInCustomer(session) != null;
    }

    private boolean isAdminSession(HttpSession session) {
        return session.getAttribute("admin") != null;
    }

    private Customer getLoggedInCustomer(HttpSession session) {
        Object user = session.getAttribute("user");
        return user instanceof Customer ? (Customer) user : null;
    }

    private String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }
}
