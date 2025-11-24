package com.inventario.controller;

import com.inventario.service.EstadisticasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private EstadisticasService estadisticasService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        Map<String, Object> estadisticas = estadisticasService.obtenerEstadisticas();
        model.addAllAttributes(estadisticas);
        return "dashboard";
    }
}
