import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

public class Practica1 {
    static Integer multiplyByTwo (Integer value){
      return value * 2;
    };

    // Método recibe un flujo de enteros, y devuelve un flujo de enteros pero sin 0.
    Flux<Integer> withoutZeroFlux(Flux<Integer> enteros){
        return enteros
                .map(Object::toString)
                .map((String value)-> value.replaceAll("0",""))
                .map(Integer::valueOf);
    }

    // Método recibe un flujo de enteros entre el rango de 0..100. Para valores menores
    // que 0 devuelve 0, y para mayores que 100, 100.
    Flux<Integer> inRangeFlux(Flux<Integer> enteros){
        return enteros.
                map((Integer value)->{
                    if(value < 0){
                        return 0;
                    }
                    else if(value > 100){
                        return 100;
                    }
                    return value;
                });
    }

    // Toma un flujo de enteros, y retorna un flujo con el doble de su valor.
    Flux<Integer> multipliedValue(Flux<Integer> enteros){
        return enteros.
                map(Practica1::multiplyByTwo);
    }

    // Toma un flujo de enteros, y retorna su primer valor.
    Flux<Integer> first(Flux<Integer> enteros){
        return enteros.take(1);
    }

    // Toma un flujo de enteros, y retorna su último valor.
    Flux<Integer> lastest(Flux<Integer> enteros){
        return enteros.takeLast(1);
    }

    // Toma un flujo de enteros y retorna el doble de los pares. Si el
    // Elemento es mayor a 1000, entonces se aborta el flujo con error.
    Flux<Integer> dobleDePares(Flux<Integer> enteros){
        return enteros
                .handle((Integer value, SynchronousSink<Integer> sink)->{
                    if(value > 1000){
                        sink.error(new RuntimeException("Valor mayor a 1000"));
                    }
                    else{
                        sink.next(value);
                    }
                })
                .filter((Integer value)->{
                    return value % 2 == 0;
                })
                .map(Practica1::multiplyByTwo);
    }
}
