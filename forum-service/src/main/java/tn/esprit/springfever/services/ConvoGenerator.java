package tn.esprit.springfever.services;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.UUID;

public class ConvoGenerator implements IdentifierGenerator {
    private static final String PREFIX = "cnv-";
    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return PREFIX + UUID.randomUUID().toString();
    }
}
