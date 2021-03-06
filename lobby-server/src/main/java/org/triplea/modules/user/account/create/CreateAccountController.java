package org.triplea.modules.user.account.create;

import com.google.common.base.Preconditions;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import lombok.Builder;
import org.jdbi.v3.core.Jdbi;
import org.triplea.http.HttpController;
import org.triplea.http.client.lobby.login.CreateAccountRequest;
import org.triplea.http.client.lobby.login.CreateAccountResponse;
import org.triplea.http.client.lobby.login.LobbyLoginClient;

@Builder
public class CreateAccountController extends HttpController {

  @Nonnull private final Function<CreateAccountRequest, CreateAccountResponse> createAccountModule;

  public static CreateAccountController build(final Jdbi jdbi) {
    return CreateAccountController.builder()
        .createAccountModule(CreateAccountModule.build(jdbi))
        .build();
  }

  @POST
  @Path(LobbyLoginClient.CREATE_ACCOUNT)
  public CreateAccountResponse createAccount(final CreateAccountRequest createAccountRequest) {
    Preconditions.checkNotNull(createAccountRequest);
    return createAccountModule.apply(createAccountRequest);
  }
}
