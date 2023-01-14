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

import java.util.Properties;

import org.killbill.billing.osgi.libs.killbill.OSGIKillbillAPI;
import org.killbill.billing.plugin.api.notification.PluginTenantConfigurableConfigurationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetnetConfigurationHandler extends PluginTenantConfigurableConfigurationHandler<Properties> {
	private static final Logger logger = LoggerFactory.getLogger(GetnetConfigurationHandler.class);

	private final String region;

	public GetnetConfigurationHandler(String region, String pluginName, OSGIKillbillAPI osgiKillbillAPI) {
		super(pluginName, osgiKillbillAPI);
		this.region = region;
	}

	@Override
	protected Properties createConfigurable(Properties properties) {
		logger.info("New properties for Getnet region {}: {}", region, properties);
		return properties;
	}

}
