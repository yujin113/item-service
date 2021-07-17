package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemParamDto;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    //    @PostMapping("/add")
    public String addItemV1(
            @RequestParam String itemName, @RequestParam int price,
            @RequestParam Integer quantity, Model model) { //int, Integer 아무거나 사용 가능
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }

    //    @PostMapping("/add")
    public String addItemV2(
            @ModelAttribute("item") Item item) {
        itemRepository.save(item);
//        model.addAttribute("item", item); 자동 추가, 생략 가능

        return "basic/item";
    }

    //    @PostMapping("/add")
    public String addItemV3(
            @ModelAttribute Item item) {
        itemRepository.save(item);
//        model.addAttribute("item", item); 자동 추가, 생략 가능. 첫글자만 소문자로 바꿔서 넣게 됨

        return "basic/item";
    }

    @PostMapping("/add")
    public String addItemV4(Item item) { // ModelAttrbiute까지 다 생략
        itemRepository.save(item);
        return "basic/item";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable long itemId, @ModelAttribute ItemParamDto updateItem) {
        itemRepository.update(itemId, updateItem);
        return "redirect:/basic/items/{itemId}";
    }

    @RequestMapping("/{itemId}/delete")
    public String delete(@PathVariable long itemId) {
        itemRepository.delete(itemId);
        return "redirect:/basic/items";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}
