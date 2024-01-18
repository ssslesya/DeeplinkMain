package ru.olesya.Deeplink.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.olesya.Deeplink.model.Dp;
import ru.olesya.Deeplink.repo.DpRepo;
import ru.olesya.Deeplink.repo.OzonRepo;
import ru.olesya.Deeplink.repo.WbRepo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Controller
@RequestMapping("/dp")
public class BotController {
    @Autowired
    WbRepo wbRepo;
    @Autowired
    OzonRepo ozonRepo;
    @Autowired
    DpRepo dpRepo;
    //добавление диплинка в бд
    @PostMapping("/create/{id}/{mp}/{product}")
    public void createDeeplink(@PathVariable Long id, @PathVariable String mp, @PathVariable Long product){
        if(dpRepo.findByUserrAndMpAndProduct(id, mp, product)==null){
            dpRepo.save(new Dp(id, mp, product));
        }
    }
    //все диплинки пользователя
    @GetMapping("/getDps/{id}")
    public void getDps(@PathVariable Long id, HttpServletResponse response) throws IOException {
        StringBuilder res = new StringBuilder();
        int n = 0;
        for (Dp dp:dpRepo.findAllByUserr(id)) {
            n+=1;
            res.append(n).append(")http://api:8080/dp/deeplink/").append(dp.getMp()).append("/").append(dp.getProduct()).append("Deeplink\n");
        }
        response.setContentType("/dp/getDps/"+id);
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(res.toString());
        response.getWriter().flush();
    }
    //статистика всех товаров пользователя
    @GetMapping("/statistics/{id}")
    public void getStatic(@PathVariable Long id, HttpServletResponse response) throws IOException {
        StringBuilder res = new StringBuilder();
        for (Dp dp:dpRepo.findAllByUserr(id)) {
            if (Objects.equals(dp.getMp(), "wb")){
                res.append(getStaticWb(dp.getProduct())).append("\n");
            } else if (dp.getMp().equals("ozon")) {
                res.append(getStaticOzon(dp.getProduct())).append("\n");
            }
        }
        response.setContentType("/dp/statistics/"+id);
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(res.toString());
        response.getWriter().flush();
    }
    //статистика одного товара
    @GetMapping("/statistics/wb/{id}")
    public String getStaticWb(@PathVariable Long id){
        int count = wbRepo.findAllByProduct(id).size();
        return "В диплинке wb/"+id+" всего просмотров: "+count;
    }
    @GetMapping("/statistics/ozon/{id}")
    public String getStaticOzon(@PathVariable Long id){
        int count = ozonRepo.findAllByProduct(id).size();
        return "В диплинке ozon/"+id+" всего просмотров: "+count;
    }
}
