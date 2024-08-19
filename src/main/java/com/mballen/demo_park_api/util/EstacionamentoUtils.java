package com.mballen.demo_park_api.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access =  AccessLevel.PRIVATE)
public class EstacionamentoUtils {
    
    private static final double PRIMEIROS_15_MINUTES = 5.00;
    private static final double PRIMEIROS_60_MINUTES = 9.25;
    private static final double ADICIONAL_15_MINUTES = 1.75;
    //
    public static String gerarRecibor(){
        LocalDateTime date = LocalDateTime.now();
        String recibo = date.toString().substring(0,19);
        return recibo.replace("-", "")
                        .replace(":", "")
                        .replace("T", "-");
    }

     public static BigDecimal calcularCusto(LocalDateTime entrada, LocalDateTime saida) {
        long minutes = entrada.until(saida, ChronoUnit.MINUTES);
        double total = 0.0;

        if (minutes <= 15) {
            
            total =  PRIMEIROS_15_MINUTES;
            
        } else if (minutes <= 60) {
            
            total = PRIMEIROS_60_MINUTES;
            
        } else {
            
            // complete com a lógica para calcular o custo acima de 60 minutos de uso
            long totalTempoPassado = 60 - minutes;
             total = Math.ceil((totalTempoPassado / 15)) * ADICIONAL_15_MINUTES ;
            
        }

        return new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal calcularDesconto(BigDecimal valor, Long totalDeVezes) {
        
        return  valor.multiply(BigDecimal.valueOf(totalDeVezes));
    }
}
