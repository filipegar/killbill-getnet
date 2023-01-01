/*
 * Copyright 2021 The Billing Project, LLC
 *
 * The Billing Project licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.killbill.billing.plugin.getnet;

import java.util.Optional;
import java.util.UUID;

import javax.inject.Named;
import javax.inject.Singleton;

import org.jooby.MediaType;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.Status;
import org.jooby.mvc.Local;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;
import org.killbill.billing.osgi.libs.killbill.OSGIKillbillClock;
import org.killbill.billing.payment.api.PluginProperty;
import org.killbill.billing.payment.plugin.api.HostedPaymentPageFormDescriptor;
import org.killbill.billing.payment.plugin.api.PaymentPluginApiException;
import org.killbill.billing.plugin.api.PluginCallContext;
import org.killbill.billing.tenant.api.Tenant;
import org.killbill.billing.util.callcontext.CallContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

@Singleton
// Handle /plugins/killbill-gocardless/checkout
@Path("/checkout")
public class GetnetCheckoutServlet {

	private final OSGIKillbillClock clock;
	private final GetnetPaymentPluginApi getnetPaymentPluginApi;
	private static final Logger logger = LoggerFactory.getLogger(GetnetCheckoutServlet.class);

	@Inject
	public GetnetCheckoutServlet(final OSGIKillbillClock clock, final GetnetPaymentPluginApi getnetPaymentPluginApi) {
		this.clock = clock;
		this.getnetPaymentPluginApi = getnetPaymentPluginApi;
	}

	// Setting up Direct Debit mandates using Hosted Payment Pages, before a payment
	// method has been added to the account
	@POST
	public Result createSession(@Named("kbAccountId") final UUID kbAccountId,
			@Named("success_redirect_url") final Optional<String> successUrl,
			@Named("redirect_flow_description") final Optional<String> description,
			@Named("lineItemName") final Optional<String> token, @Local @Named("killbill_tenant") final Tenant tenant)
			throws PaymentPluginApiException {
		logger.info("Inside createSession");
		final CallContext context = new PluginCallContext(GetnetActivator.PLUGIN_NAME, clock.getClock().getUTCNow(),
				kbAccountId, tenant.getId());
		final ImmutableList<PluginProperty> properties = ImmutableList.of(
				new PluginProperty("success_redirect_url",
						successUrl.orElse("https://developer.gocardless.com/example-redirect-uri/"), false),
				new PluginProperty("redirect_flow_description", description.orElse("Kill Bill payment"), false),
				new PluginProperty("session_token", token.orElse("killbill_token"), false));
		final HostedPaymentPageFormDescriptor hostedPaymentPageFormDescriptor = getnetPaymentPluginApi
				.buildFormDescriptor(kbAccountId, ImmutableList.of(), properties, context);
		return Results.with(hostedPaymentPageFormDescriptor, Status.CREATED).type(MediaType.json);
	}
}
