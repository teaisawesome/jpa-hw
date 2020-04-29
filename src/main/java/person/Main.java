package person;

import com.github.javafaker.Faker;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.ZoneId;
import java.util.Locale;

@Log4j2
public class Main
{
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-example");

    private static Faker faker = new Faker(new Locale("hu"));

    private static Person randomPerson()
    {
        EntityManager em = emf.createEntityManager();

        Person person = Person.builder()
                .name(faker.name().fullName())
                .dob(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .gender(faker.options().option(Person.Gender.FEMALE, Person.Gender.MALE))
                .address(
                        Address.builder()
                        .country(faker.address().country())
                        .state(faker.address().state())
                        .city(faker.address().city())
                        .streetAddress(faker.address().streetAddress())
                        .zip(faker.address().zipCode())
                        .build()
                )
                .email(faker.internet().emailAddress())
                .profession(faker.company().profession())
                .build();
        try
        {

            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();

            return person;

        } finally {
            em.close();
        }
    }


    public static void main(String[] args)
    {
        for (int i = 0; i < 1000; i++)
        {
            log.info(randomPerson());
        }
    }
}
