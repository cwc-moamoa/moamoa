import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class SocialUserController(
) {
    @GetMapping("oauth2/login")
    fun getKakaoPage(): String {
        return "order"
    }


}