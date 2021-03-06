/**
 * Copyright 2012 52�North Initiative for Geospatial Open Source Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.n52.geoar.data.cosm;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.n52.geoar.newdata.Annotations;
import org.n52.geoar.newdata.Annotations.Settings.Name;
import org.n52.geoar.newdata.Annotations.SharedHttpClient;
import org.n52.geoar.newdata.Annotations.SupportedVisualization;
import org.n52.geoar.newdata.DataSource;
import org.n52.geoar.newdata.SpatialEntity;

@Annotations.DataSource(name = @Name("COSM"), cacheZoomLevel = 0, minReloadInterval = -1, description = "Example for a description")
@SupportedVisualization(visualizationClasses = { CosmMapVisualization.class,
		CosmARVisualization.class })
public class CosmSource implements DataSource<CosmFilter> {

	@SharedHttpClient
	private HttpClient mHttpClient;
	
	@Override
	public List<? extends SpatialEntity> getMeasurements(CosmFilter filter) {
		return searchWikilocationRequest(filter);
	}

	@Override
	public boolean isAvailable() {
		return true;
	}

	private List<? extends SpatialEntity> searchWikilocationRequest(
			CosmFilter filter) {
		String request = CosmRequest.buildRequest(filter);
		List<CosmFeed> resultList = null;
		InputStream content = null;
		try {
			HttpResponse response = mHttpClient.execute(new HttpGet(request));
			content = response.getEntity().getContent();
			resultList = CosmResponse.getCosmFeedsFromResponse(content);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultList;
	}
}
