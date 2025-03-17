package com.example.shop.Item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class ItemController {



    private final ItemRepository itemRepository;
    //오브젝트 미리 뽑기
    private final ItemService itemService;

    private final SupabaseService supabaseService;



    /*
    @GetMapping("/list")
    String list(Model model){
        List<Item> result = itemRepository.findAll();

        model.addAttribute("items", result);
        //result를 items란 이름으로 list.html에서 사용 가능


        return "list.html";
    }

     */

    //상품추가기능
    //1. 상품 이름, 가격 작성할 수 있는 페이지와 폼
    //2. 전송버튼 누르면 서버로 보냄
    //3. 서버는 검사 후 이상없으면 DB로 보냄

    // write 접속하면 폼 보내줌
    @GetMapping("/write")
    String write(){
        return "write.html";
    }

    //write.html에서 보낸 데이터를 formData로 받음. key 값은 input의 name attr임
    @PostMapping("/add")
    public String addPost(@RequestParam Map<String, String> formData, Authentication auth){

        String username;

        //하나의 함수엔 하나의 기능만 담는게 좋다.
        if (auth != null) {
            username = auth.getName();
        } else{
            username = "null";
        }
        itemService.saveItem(formData, username);

        return "redirect:/list/page/1";
    }

    //@PathVariable을 사용하면 {}안의 값을 사용할 수 있다.
    @GetMapping("/detail/{id}")
    String detail(@PathVariable Long id, Model model){

            Optional<Item> result = itemRepository.findById(id);
            if (result.isPresent()) {
                model.addAttribute("items", result.get());

                return "detail.html";
            } else {
                return "redirect:/list";
            }
    }

    //1. 수정버튼
    //2. 수정 폼
    //3. 전송누르면 DB 수정

    @GetMapping("/edit/{id}")
    String edit(@PathVariable Long id, Model model){

        Optional<Item> result = itemRepository.findById(id);
        if (result.isPresent()){
            model.addAttribute("items", result.get());
            return "edit.html";
        }else{
            return "redirect:/list";
        }

    }

    @PostMapping("/edit")
    String editPost(@RequestParam Map<String, String> formData){

        itemService.editItem(formData);

        return "redirect:/list";
    }

    @DeleteMapping("delete")
     ResponseEntity<String> deleteItem(@RequestParam Long id){

        itemRepository.deleteById(id);

        return ResponseEntity.status(200).body("삭제완료");

    }

    @GetMapping("/list/page/{page}")
    String getListPage(Model model, @PathVariable Integer page){

        Page<Item> result = itemRepository.findPageBy(PageRequest.of(page - 1, 5));
        model.addAttribute("items", result);
        model.addAttribute("pages", result.getTotalPages());
        return "list.html";

    }


    @GetMapping("/presigned-url")
    String getURL(@RequestParam String filename){
        try {
            // Supabase 스토리지에 저장된 파일의 presigned URL 생성
            var result = supabaseService.createPresignedUrl("test/" + filename);
            System.out.println(result); // 로그 출력
            return result;
        } catch (IOException e) {

            return "Error generating presigned URL: " + e.getMessage();
    }
}}

