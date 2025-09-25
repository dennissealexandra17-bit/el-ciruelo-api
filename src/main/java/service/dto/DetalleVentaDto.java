package service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetalleVentaDto {

    private Long id;
    private Long productoId;
    private String productoNombre;
    private String productoMarca;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}
