package io.github.rxcats.datasourceroutedemo.ds;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Component
@Scope("prototype")
public class MyTransactionManager extends DefaultTransactionDefinition {
	private static final long serialVersionUID = 3210356049347819869L;

	@Autowired
	private PlatformTransactionManager transactionManager;

	private TransactionStatus status;

	public void start() throws TransactionException {
		status = transactionManager.getTransaction(this);
	}

	public boolean completed() {
		return status.isCompleted();
	}

	public void commit() throws TransactionException {
		if (!status.isCompleted()) {
			transactionManager.commit(status);
		}
	}

	public void rollback() throws TransactionException {
		if (!status.isCompleted()) {
			transactionManager.rollback(status);
		}
	}

	public void close() throws TransactionException {
		rollback();
	}
}
