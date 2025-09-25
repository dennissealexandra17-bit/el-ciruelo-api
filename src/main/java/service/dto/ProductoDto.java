package service.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProductoDto {
    private Long id;
    private String nombre;
    private Integer stock;
        private Integer stockInicial;

    private Double precioCompra;
    private Double precioVenta;
    private String marca;
    private String categoria;
    private Integer cantidadMinima;

    private LocalDateTime fechaIngreso;
    


}
