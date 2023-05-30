package kr.ac.kopo.guestbook.controller;


import kr.ac.kopo.guestbook.dto.GuestbookDTO;
import kr.ac.kopo.guestbook.dto.PageRequestDTO;
import kr.ac.kopo.guestbook.dto.PageResultDTO;
import kr.ac.kopo.guestbook.service.GuestbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor
public class GuestbookController {

    private final GuestbookService service;

    @GetMapping("/")
    public String index() {
        return "redirect:/guestbook/list";
    }

    @GetMapping( "/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){

        log.info("방명록 목록 화면 " + pageRequestDTO);

        model.addAttribute("result", service.getList(pageRequestDTO));

    }

    // 등록 화면 보여줌
    @GetMapping("/register")
    public void register(){
        log.info("register get...");
    }

    // 등록 처리 후 목록 페이지로 이동
    @PostMapping("/register")
    public String registerPost(GuestbookDTO dto, RedirectAttributes redirectAttribute){
        log.info("dto ..." + dto);

        // 새로 추가된 엔티티 번호
        // register메소드는 전달받은 dto를 entity로 변환해서 DB에 저장
        Long gno = service.register(dto);

        redirectAttribute.addFlashAttribute("msg", gno);

        return "redirect:/guestbook/list";
    }

    @GetMapping({"/read", "/modify"})
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {
        log.info("gno : " + gno);

        GuestbookDTO dto = service.read(gno);

        model.addAttribute("dto", dto);
    }

    @PostMapping("/modify")
    public String moify(GuestbookDTO dto, @ModelAttribute("requestDto") PageRequestDTO requestDTO,
        RedirectAttributes redirectAttributes) {

        log.info("post modify................");
        log.info("dto : " + dto);

        service.modify(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("gno", dto.getGno());

        return "redirect:/guestbook/read";

    }


    @PostMapping("/remove")
    public String remove(long gno, RedirectAttributes redirectAttributes){

        log.info("gno : " + gno);

        service.remove(gno);

        redirectAttributes.addFlashAttribute("msg", gno);

        return "redirect:/guestbook/list";

    }

}
