package pe.edu.vallegrande.Backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @GetMapping
    public String obtenerProductos() {
        return "¡Hola, tu controlador está funcionando!";
    }
}