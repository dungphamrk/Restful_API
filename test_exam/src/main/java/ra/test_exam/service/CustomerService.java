package ra.test_exam.service;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import ra.test_exam.model.dto.request.CustomerLogin;
import ra.test_exam.model.dto.request.CustomerPrincipal;
import ra.test_exam.model.dto.response.JWTResponse;
import ra.test_exam.model.entity.Customer;
import ra.test_exam.sercurity.principal.CustomUserDetails;

public interface CustomerService {
    Customer registerCustomer(CustomerPrincipal userRegister);

    JWTResponse login(CustomerLogin userLogin);

    JWTResponse logout(@AuthenticationPrincipal CustomUserDetails customUserDetails);
}
