package service.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InventarioDto {
    
     private Long id;

    private Long productoId;

    private int cantidad;

    private int cantidadMinima;

private String productoNombre;

private String productoMarca;

    public InventarioDto() {
    }

    public InventarioDto(Long id, Long productoId, int cantidad, int cantidadMinima, String productoNombre, String productoMarca) {
        this.id = id;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.cantidadMinima = cantidadMinima;
        this.productoNombre = productoNombre;
        this.productoMarca = productoMarca;
    }

}
