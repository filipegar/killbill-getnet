/*******************************************************************************
 * Copyright (c) 2023 Filipe Garcia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX short identifier: Apache-2.0
 *
 * Contributors:
 *     Filipe Garcia - initial API and implementation
 *******************************************************************************/
package org.killbill.billing.plugin.getnet;

import java.util.Hashtable;

import org.killbill.billing.osgi.api.OSGIPluginProperties;
import org.killbill.billing.osgi.libs.killbill.KillbillActivatorBase;
import org.killbill.billing.payment.plugin.api.PaymentPluginApi;
import org.killbill.billing.plugin.getnet.dao.GetnetDao;
import org.osgi.framework.BundleContext;

public class GetnetActivator extends KillbillActivatorBase {

	// This is the plugin name and is used by Kill Bill to route payment to the
	// appropriate payment plugin
	public static final String PLUGIN_NAME = "killbill-getnet";
	public static final String PROPERTY_PREFIX = "org.killbill.billing.plugin.getnet.";

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);

		final GetnetDao getnetDao = new GetnetDao(dataSource.getDataSource());

		final GetnetPaymentPluginApi pluginApi = new GetnetPaymentPluginApi(killbillAPI, clock.getClock(),
				configProperties, getnetDao);
		registerPaymentPluginApi(context, pluginApi);
	}

	private void registerPaymentPluginApi(final BundleContext context, final PaymentPluginApi api) {
		final Hashtable<String, String> props = new Hashtable<String, String>();
		props.put(OSGIPluginProperties.PLUGIN_NAME_PROP, PLUGIN_NAME);
		registrar.registerService(context, PaymentPluginApi.class, api, props);
	}
}
