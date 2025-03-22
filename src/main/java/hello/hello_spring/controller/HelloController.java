package hello.hello_spring.controller;

import hello.hello_spring.dto.Hello;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {


    @Operation(summary = "테스트용 API: 이 api는 사람이름 넣어서 쓰시고, 숫자 넣지 마세요.")
    @GetMapping("hello-api/{name}")
    @ResponseBody
    public Hello helloApi(@PathVariable("name") String name){
        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }
    @Operation(summary = "테스트용 API임")
    @GetMapping("hello2/{name}")
    @ResponseBody
    public Hello helloHi(@PathVariable("name") String name){
        Hello hello = new Hello();
        hello.setName(name+"2222");
        return hello;
    }



}
