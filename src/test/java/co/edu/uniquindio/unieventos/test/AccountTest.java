package co.edu.uniquindio.unieventos.test;

import co.edu.uniquindio.unieventos.model.documents.Account;
import co.edu.uniquindio.unieventos.model.enums.AccountStatus;
import co.edu.uniquindio.unieventos.model.enums.Role;
import co.edu.uniquindio.unieventos.model.vo.User;
import co.edu.uniquindio.unieventos.repositories.AccountRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class AccountTest {

    @Autowired
    private AccountRepo accountRepo;
    @Test
    public void registerAccountTest(){
        User user= User.builder()
                .id("222222")
                .phoneNumber("3184563245")
                .adress("Barrio Fundadores Mz4 #5")
                .dni("222222")
                .name("Pepe pepin")
                .build();
        Account account= Account.builder()
                .user(user)
                .email("pepin@gmail.com")
                .password("4444")
                .role(Role.CLIENT)
                .registrationDate(LocalDateTime.now())
                .status(AccountStatus.ACTIVE)
                .build();
        //Account is saved on the DB
        Account accountRegister= accountRepo.save(account);
        //Verification if the saved object is not null
        Assertions.assertNotNull(accountRegister);
    }
    @Test
    public void updateAccountTest(){
        //Ger the account with the given id
        //Replace with the id with the local db on the other pcs
        Account account= accountRepo.findById("66cff61aef5f356eab91fd74").orElseThrow();


        //Gonna modify the email of the account
        account.setEmail("juanmartinezz@gmail.com"); //This was already updated

        //Save the account again (Save is gonna update the data
        accountRepo.save(account);
        //Replace with the id with the local db on the other pcs
        Account accountUpdate=accountRepo.findById("66cff61aef5f356eab91fd74").orElseThrow();
        //Replace with the id with the local db on the other pcs
        Assertions.assertEquals("juanmartinezz@gmail.com", accountUpdate.getEmail());
    }
    @Test
    public void listAllAccountsTest(){
        //Obtain the list of all accounts (for now we only have 1)
        List<Account> accounts=accountRepo.findAll();
        //We print the accounts, we use a lambda function
        accounts.forEach(System.out::println);
        //We verify that only one account exists.
        Assertions.assertEquals(1, accounts.size());

    }
    @Test
    public void deleteByIdTest(){
        //The pepe pepin id on my db (First time i created it)
        //Replace with a value of an _id of your db
        //Delete the user with the id XXXXXXX
        accountRepo.deleteById("66cffa3676e4877940a2f5dd");
        //Obtain the user with the id XXXXXXX
        Account account=accountRepo.findById("66cffa3676e4877940a2f5dd").orElse(null);
        //Verify that the user does not exist (is null) since it was deleted.
        Assertions.assertNull(account);
    }
}
