package com.example.Rabota.Controller;

import com.example.Rabota.Models.Game;
import com.example.Rabota.Models.Specifications;
import com.example.Rabota.repo.SpecsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class SpecsController {
    @Autowired
    private SpecsRepository specsRepository;
    @GetMapping("/Specs")
    public String GetSpecs(Model model)
    {
        Iterable<Specifications> specifications = specsRepository.findAll();
        model.addAttribute("Specifications",specifications);
        return "MainSpecs";
    }
    @GetMapping("/Add/Specs")
    public String GetAddSpecs(Model model)
    {
        return "Add-Specs";
    }
    @PostMapping("/Add/Specs")
    public String blogPostAdd(@RequestParam(value="CPU") String CPU,
                              @RequestParam(value ="RAM" ) int RAM,
                              @RequestParam(value = "MemorySize") String MemorySize,
                              @RequestParam(value ="GPU" ) String GPU,
                              @RequestParam(value = "OS") String OS,
                              Model model)
    {
        Specifications specifications = new Specifications(CPU, RAM, MemorySize, GPU, OS);
        specsRepository.save(specifications);
        return "redirect:/Specs";
    }
    @GetMapping( path = "/Search/Specs")
    public String blogFilter(Model model)
    {
        return "Search-Specs";
    }

    @PostMapping("/Search/Specs-result")
    public String blogResult(@RequestParam String CPU, Model model)
    {
        List<Specifications> specifications = specsRepository.findByCPU(CPU);
        model.addAttribute("result", specifications);
        return "Search-Specs";
    }
    @PostMapping("/Search/Specs")
    public String simpleSearch(@RequestParam String GPU, Model model) {
        List<Specifications> specifications = specsRepository.findByGPU(GPU);
        model.addAttribute("result", specifications);
        return "Search-Specs";
    }

    @GetMapping("/blog/Specs/{id}/Edit")
    public String SpecsEdit(@PathVariable(value = "id") Long id, Model model) {
        if (!specsRepository.existsById(id)) {
            return "redirect:/Specs";
        }
        Optional<Specifications> specifications = specsRepository.findById(id);
        ArrayList<Specifications> lis = new ArrayList<>();
        specifications.ifPresent(lis::add);
        model.addAttribute("specs", lis);
        return "Edit-Specs";
    }

    @PostMapping("/blog/Specs/{id}/Edit")
    public String SpecsUpdate(
            @PathVariable(value = "id") Long id,
            @RequestParam(value="CPU") String CPU,
            @RequestParam(value ="RAM" ) int RAM,
            @RequestParam(value = "MemorySize") String MemorySize,
            @RequestParam(value ="GPU") String GPU,
            @RequestParam(value = "OS") String OS,
            Model model){
        Specifications specifications = specsRepository.findById(id).orElseThrow();
        specifications.setCPU(CPU);
        specifications.setRAM(RAM);
        specifications.setMemorySize(MemorySize);
        specifications.setGPU(GPU);
        specifications.setOS(OS);
        specsRepository.save(specifications);
        return "redirect:/Specs";
    }

    @PostMapping("/blog/Specs/{id}/Remove")
    public String blogSpecsDelete(
            @PathVariable(value = "id") Long id,
            Model model) {
        Specifications specifications = specsRepository.findById(id).orElseThrow();
        specsRepository.delete(specifications);
        return "redirect:/Specs";
    }

}
