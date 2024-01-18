package ru.olesya.Deeplink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.olesya.Deeplink.model.Dp;
import ru.olesya.Deeplink.model.Ozon;
import ru.olesya.Deeplink.model.Wb;
import ru.olesya.Deeplink.repo.DpRepo;
import ru.olesya.Deeplink.repo.OzonRepo;
import ru.olesya.Deeplink.repo.WbRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
//https://www.wildberries.ru/catalog/146846433/detail.aspx
//https://www.ozon.ru/product/838225572
//

@Controller
@RequestMapping("/dp")

public class DeeplinkController {
    @Autowired
    WbRepo wbRepo;
    @Autowired
    OzonRepo ozonRepo;
    @GetMapping("/deeplink/{mp}/{id}")
    public String getPage(@PathVariable String mp,@PathVariable Long id, Model model){
        if (Objects.equals(mp, "ozon")){
            ozonRepo.save(new Ozon(LocalDateTime.now(), id));
            model.addAttribute("link", "https://www.ozon.ru/product/"+id);
        }
        if(Objects.equals(mp, "wb")){
            wbRepo.save(new Wb(LocalDateTime.now(), id));
            model.addAttribute("link", "https://www.wildberries.ru/catalog/"+id+"/detail.aspx");
        }
        return "page";
    }
}
