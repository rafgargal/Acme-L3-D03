
package acme.features.authenticated.lecturer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.components.accounts.Authenticated;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.BinderHelper;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class AuthenticatedLecturerUpdateService extends AbstractService<Authenticated, Lecturer> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedLecturerRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void load() {
		Lecturer object;
		Principal principal;
		int userAccountId;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		object = this.repository.findOneLecturerByUserAccountId(userAccountId);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Lecturer object) {
		assert object != null;

		super.bind(object, "almaMater", "resume", "qualificationsList", "furtherInfo");
	}

	@Override
	public void validate(final Lecturer object) {
		assert object != null;
	}

	@Override
	public void perform(final Lecturer object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Lecturer object) {
		assert object != null;

		Tuple tuple;

		tuple = BinderHelper.unbind(object, "almaMater", "resume", "qualificationsList", "furtherInfo");
		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
