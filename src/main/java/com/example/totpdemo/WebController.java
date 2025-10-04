package com.example.totpdemo;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Controller
public class WebController {

    @Autowired
    private UserService userService;

    @Autowired
    private CodeVerifier codeVerifier;

    @Value("${totp.issuer}")
    private String issuer;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password, Model model) {
        if (userService.findByUsername(username) != null) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        SecretGenerator secretGenerator = new DefaultSecretGenerator();
        String secret = secretGenerator.generate();

        userService.registerUser(username, password, secret);

        QrData data = new QrData.Builder()
                .label(username)
                .secret(secret)
                .issuer(issuer)
                .build();

        QrGenerator generator = new ZxingPngQrGenerator();
        String qrCodeImage;
        try {
            qrCodeImage = getDataUriForImage(generator.generate(data), generator.getImageMimeType());
        } catch (QrGenerationException e) {
            model.addAttribute("error", "Failed to generate QR code");
            return "register";
        }

        model.addAttribute("qrCode", qrCodeImage);
        model.addAttribute("secret", secret);
        return "setup-totp";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        User user = userService.findByUsername(username);
        if (user == null || !password.equals(user.getPassword())) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }

        model.addAttribute("username", username);
        return "verify-totp";
    }

    @PostMapping("/verify")
    public String verify(@RequestParam String username, @RequestParam String code, Model model) {
        User user = userService.findByUsername(username);
        if (user == null) {
            model.addAttribute("error", "User not found.");
            return "login";
        }

        if (!codeVerifier.isValidCode(user.getSecret(), code)) {
            model.addAttribute("error", "Invalid TOTP code.");
            model.addAttribute("username", username);
            return "verify-totp";
        }

        user.setMfaEnabled(true);
        return "success";
    }
}