package com.example.shop.Item;

//페이지를 보내기 전에 이거저거 검사하거나
//DB 입출력하거나 하는 것을 비즈니스 로직이라고 함
//비즈니스 로직 담는 클래스는 Service라고 부름

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public void saveItem(@RequestParam Map<String, String> formData, String username){

        Item item = new Item();
        item.setTitle(formData.get("title"));
        item.setPrice(Integer.parseInt(formData.get("price")));
        item.setUsername(username);
        itemRepository.save(item);

    }

    public void editItem(@RequestParam Map<String, String> formData) {

            Item item = new Item();
            item.setId(Long.parseLong(formData.get("id")));
            item.setTitle(formData.get("title"));
            item.setPrice(Integer.parseInt(formData.get("price")));
            itemRepository.save(item);
        }


}
