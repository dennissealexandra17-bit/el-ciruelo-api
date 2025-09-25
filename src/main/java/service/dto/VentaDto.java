package service.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VentaDto {
    
     private Long id;
    private LocalDateTime fecha;
    private Double total;
    private List<DetalleVentaDto> detalles;
}
