package co.edu.uniquindio.unieventos.test;

import co.edu.uniquindio.unieventos.services.implementations.AccountServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountServiceImp accountServiceImp;
}
