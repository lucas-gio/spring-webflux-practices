import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
class Tests {
    @Test
    // Con flujo vacío, se completa sin tener resultados
    void withoutZeroFlux__WithEmptyFlux(){
        StepVerifier
                .create(new Practica1().withoutZeroFlux(Flux.empty()))
                .expectComplete()
                .verify();
    }

    @Test
    // Con flujo con varios valores, se obtienen los resultados esperados.
    void withoutZeroFlux__WithFullFlux(){
        StepVerifier
                .create(new Practica1().withoutZeroFlux(Flux.just(-1, 1, 10, 100, 101, 1010)))
                .expectNext(-1, 1, 1, 1, 11, 11)
                .expectComplete()
                .verify();
    }

    @Test
    // Con flujo con varios valores, se obtienen los resultados esperados, hasta que llega un 0 y genera
    // un fallo por hacer Integer.valueOf(0.toString().replace(0,""))
    void withoutZeroFlux__WithErrorFullFlux(){
        StepVerifier
                .create(new Practica1().withoutZeroFlux(Flux.just(-1, 1, 0, 100, 101, 1010)))
                .expectNext(-1, 1)
                .expectError()
                .verify();
    }

    @Test
    // Prueba inRangeFlux. Caso flujo vacío.
    void inRangeFlux__withEmptyFlux(){
        StepVerifier
                .create(new Practica1().inRangeFlux(Flux.empty()))
                .expectComplete()
                .verify();
    }

    @Test
    void inRangeFlux__withFullFlux(){
        // Prueba inRangeFlux, con caso de valores borde.
        StepVerifier
                .create(new Practica1().inRangeFlux(Flux.just(-1, 0, 1, 99, 100, 101)))
                .expectNext(0, 0, 1, 99, 100, 100)
                .expectComplete()
                .verify();
    }

    @Test
    void multipliedValue__withEmptyFlux(){
        // Prueba inRangeFlux, con caso de valores borde.
        StepVerifier
                .create(new Practica1().multipliedValue(Flux.empty()))
                .expectComplete()
                .verify();
    }

    @Test
    void multipliedValue__withFullFlux(){
        // Debe retornar el doble de su valor, en to do caso.
        StepVerifier
                .create(new Practica1().multipliedValue(Flux.just(-1, 0, 1, 2, 3, 4, 5)))
                .expectNext(-2, 0, 2, 4, 6, 8, 10)
                .expectComplete()
                .verify();
    }

    @Test
    void first__withEmptyFlux(){
        // Con flujo vacío, se retoma el control sin errores.
        StepVerifier
                .create(new Practica1().first(Flux.empty()))
                .expectComplete()
                .verify();
    }

    @Test
    void first__withFullFlux(){
        // Debe retornar el primero de los valores.
        StepVerifier
                .create(new Practica1().first(Flux.just(1, 2, 3, 4, 5, 6)))
                .expectNext(1)
                .expectComplete()
                .verify();
    }

    @Test
    void lastest__withEmptyFlux(){
        // Con flujo vacío, se retoma el control sin errores.
        StepVerifier
                .create(new Practica1().lastest(Flux.empty()))
                .expectComplete()
                .verify();
    }

    @Test
    void lastest__withFullFlux(){
        // Debe retornar el último de los valores.
        StepVerifier
                .create(new Practica1().lastest(Flux.just(1, 2, 3, 4, 5, 6)))
                .expectNext(6)
                .expectComplete()
                .verify();
    }

    @Test
    void dobleDePares__withEmptyFlux(){
        // Con flujo vacío, se retoma el control sin errores.
        StepVerifier
                .create(new Practica1().dobleDePares(Flux.empty()))
                .expectComplete()
                .verify();
    }

    @Test
    void dobleDePares__withFullFluxOK(){
        // Debe retornar el valor *2 de los nros pares. Si el nro original es mayor a 1000, se espera
        // que se aborte el flujo.
        StepVerifier
                .create(new Practica1().dobleDePares(Flux.just(-1, 0, 1, 100, 999, 1000)))
                .expectNext(0, 200, 2000)
                .expectComplete()
                .verify();
    }

    @Test
    void dobleDePares__withFullFluxWithErrors(){
        // Debe retornar el valor *2 de los nros pares. Si el nro original es mayor a 1000, se espera
        // que se aborte el flujo. Caso con errores.
        StepVerifier
                .create(new Practica1().dobleDePares(Flux.just(-1, 0, 1, 100, 2, 999, 1000, 1001, 8, 4, 10)))
                .expectNext(0, 200, 4, 2000)
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Valor mayor a 1000")
                )
                .verify();
    }
}
