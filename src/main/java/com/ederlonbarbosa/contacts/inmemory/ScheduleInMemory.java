package com.ederlonbarbosa.contacts.inmemory;

import com.ederlonbarbosa.contacts.base.exception.BusinessException;
import com.ederlonbarbosa.contacts.base.model.Contact;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ederlon Barbosa
 */
public class ScheduleInMemory {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleInMemory.class);

    private Map<String, Set<Contact>> scheduleMap;

    public ScheduleInMemory(final String ownerName) {
        validateOwnerName(ownerName);
        scheduleMap = new HashMap<>();
        scheduleMap.put(ownerName, new HashSet<>());
    }

    private void validateOwnerName(final String ownerName) {
        if (Strings.isBlank(ownerName)) {
            LOG.error("The ownerName {} is invalid.", ownerName);
            throw new BusinessException("OwnerName cannot be empty.");
        }
        if (this.scheduleMap != null && this.scheduleMap.get(ownerName) == null) {
            LOG.error("The ownerName {} has not been registered.", ownerName);
            throw new BusinessException("The ownerName has not been registered.");
        }
    }

    /**
     * Returns the number of contacts in a schedule.
     *
     * @param ownerName
     * @return
     */
    public int getContactsSize(final String ownerName) {
        validateOwnerName(ownerName);
        return this.scheduleMap.get(ownerName).size();
    }

    /**
     * return unchanging contact list
     *
     * @return
     */
    public Set<Contact> getContacts(final String ownerName) {
        validateOwnerName(ownerName);
        return Collections.unmodifiableSet(this.scheduleMap.get(ownerName));
    }

    /**
     * add a new contact
     *
     * @param contact
     * @return
     */
    public Set<Contact> add(final String ownerName, final Contact contact) {

        validateOwnerName(ownerName);
        validateContactAlreadyExists(ownerName, contact);

        final boolean hasError = !this.scheduleMap.get(ownerName).add(contact);
        if (hasError) {
            LOG.error("Error to add {}", contact);
            throw new BusinessException("Contact already exists.");
        }
        return getContacts(ownerName);
    }

    private void validateContactAlreadyExists(final String ownerName, final Contact newContact) {
        for (Contact contact : this.scheduleMap.get(ownerName)) {
            if (newContact.getCellphone().equals(contact.getCellphone())) {
                LOG.error("Contact already exists: {}", newContact);
                throw new BusinessException("Contact already exists.");
            }
        }
    }

    /**
     * remove a contact
     *
     * @param contact
     * @return
     */
    public Set<Contact> remove(final String ownerName, final Contact contact) {
        validateOwnerName(ownerName);
        final boolean hasError = !this.scheduleMap.get(ownerName).remove(contact);
        if (hasError) {
            LOG.error("Contact does not exist: {}", contact);
            throw new BusinessException("Contact does not exist.");
        }
        return getContacts(ownerName);
    }

    /**
     * remove a updatedContact
     *
     * @param updatedContact
     * @return
     */
    public Set<Contact> update(final String ownerName, final Contact updatedContact) {
        validateOwnerName(ownerName);
        this.scheduleMap.get(ownerName).forEach(contact -> {
            if (contact.getCellphone().equals(updatedContact.getCellphone())) {
                remove(ownerName, contact);
                add(ownerName, updatedContact);
            }
        });
        return getContacts(ownerName);
    }

    /**
     * get contact by cellphone
     *
     * @param cellPhone
     * @return
     */
    public Optional<Contact> get(final String ownerName, final String cellPhone) {
        validateOwnerName(ownerName);
        AtomicReference<Optional<Contact>> contactOptional = new AtomicReference<>(Optional.empty());
        this.scheduleMap.get(ownerName).forEach(contact -> {
            if (contact.getCellphone().equals(cellPhone)) {
                contactOptional.set(Optional.of(contact));
            }
        });
        return contactOptional.get();
    }
}
