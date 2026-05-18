package lk.sliit.it25.bakeryweb.customrequests;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/custom-requests")
public class CustomRequestController {

    private static final String FILE_PATH = "data/custom_requests.txt";

    // 1. View All Requests
    @GetMapping
    public String viewRequests(Model model) {
        List<CustomRequest> list = readRequestsFromFile();
        model.addAttribute("requests", list);
        return "customrequests/view";
    }

    // 2. Show Submit Form
    @GetMapping("/submit")
    public String showSubmitForm() {
        return "customrequests/submit";
    }

    // 3. Save Submitted Request to Text File
    @PostMapping("/submit")
    public String submitRequest(@RequestParam String requestId,
                                @RequestParam String customerName,
                                @RequestParam String tiers,
                                @RequestParam String theme) {
        String record = requestId + "," + customerName + "," + tiers + "," + theme + ",Pending\n";
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
        return "redirect:/custom-requests";
    }

    // 4. Show Edit Form
    @GetMapping("/edit")
    public String showEditForm(@RequestParam String id, Model model) {
        List<CustomRequest> list = readRequestsFromFile();
        for (CustomRequest req : list) {
            if (req.getRequestId().equals(id)) {
                model.addAttribute("request", req);
                break;
            }
        }
        return "customrequests/edit";
    }

    // 5. Save Edited Request back to File
    @PostMapping("/edit")
    public String editRequest(@RequestParam String requestId,
                              @RequestParam String customerName,
                              @RequestParam String tiers,
                              @RequestParam String theme,
                              @RequestParam String status) {
        List<CustomRequest> list = readRequestsFromFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (CustomRequest req : list) {
                if (req.getRequestId().equals(requestId)) {
                    writer.write(requestId + "," + customerName + "," + tiers + "," + theme + "," + status + "\n");
                } else {
                    writer.write(req.getRequestId() + "," + req.getCustomerName() + "," + req.getTiers() + "," + req.getTheme() + "," + req.getStatus() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}