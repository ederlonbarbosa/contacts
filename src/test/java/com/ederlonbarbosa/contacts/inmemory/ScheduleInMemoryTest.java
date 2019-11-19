package com.ederlonbarbosa.contacts.inmemory;

import com.ederlonbarbosa.contacts.base.exception.BusinessException;
import com.ederlonbarbosa.contacts.base.model.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ederlon Barbosa
 */
class ScheduleInMemoryTest {

    private ScheduleInMemory schedule;
    private final String cellphoneJohn = "19 99999-9999";
    private final Contact john = new Contact("John", cellphoneJohn);
    private final Contact mary = new Contact("Mary", "19 91111-1111");
    private final String ownerName = "Paul";

    @BeforeEach
    private void before() {
        schedule = new ScheduleInMemory(ownerName);
    }

    @Test
    public void addTest() {
        schedule.add(ownerName, john);
        assertThat(schedule.getContactsSize(ownerName) == 1, is(true));
    }

    @Test
    public void addOwnerNameInvalidTest() {

        BusinessException thrown =
                assertThrows(BusinessException.class,
                        () -> schedule.add("ownerName", john)
                );
        assertTrue(thrown.getMessage().equals("The ownerName has not been registered."));
    }

    @Test
    public void addDuplicatesTest() {
        schedule.add(ownerName, john);
        schedule.add(ownerName, mary);

        BusinessException thrown =
                assertThrows(BusinessException.class,
                        () -> schedule.add(ownerName, john)
                );
        assertTrue(thrown.getMessage().equals("Contact already exists."));
    }

    @Test
    public void removeTest() {
        schedule.add(ownerName, john);
        schedule.remove(ownerName, john);
        assertThat(schedule.getContactsSize(ownerName) == 0, is(true));
    }

    @Test
    public void removeOwnerNameInvalidTest() {

        BusinessException thrown =
                assertThrows(BusinessException.class,
                        () -> schedule.add("", john)
                );
        assertTrue(thrown.getMessage().equals("OwnerName cannot be empty."));
    }

    @Test
    public void removeNonexistentContactTest() {

        schedule.add(ownerName, john);
        BusinessException thrown =
                assertThrows(BusinessException.class,
                        () -> schedule.remove(ownerName, mary)
                );

        assertTrue(thrown.getMessage().equals("Contact does not exist."));
    }

    @Test
    public void getTest() {
        schedule.add(ownerName, john);
        final Optional<Contact> contactOptional = schedule.get(ownerName, cellphoneJohn);
        assertThat(john.getCellphone().equals(contactOptional.get().getCellphone()), is(true));
    }

    @Test
    public void getEmptyTest() {
        final Optional<Contact> contactOptional = schedule.get(ownerName, cellphoneJohn);
        assertThat(contactOptional.isEmpty(), is(true));
    }

    @Test
    public void updateTest() {
        schedule.add(ownerName, john);
        final String newName = "John Brown";
        john.setName(newName);
        schedule.update(ownerName, john);
        final Contact contact = schedule.get(ownerName, john.getCellphone()).get();
        assertThat(contact.getName().equals(newName), is(true));
    }

}