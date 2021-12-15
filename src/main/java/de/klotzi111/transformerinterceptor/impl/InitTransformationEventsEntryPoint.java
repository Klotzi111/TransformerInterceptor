package de.klotzi111.transformerinterceptor.impl;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class InitTransformationEventsEntryPoint implements PreLaunchEntrypoint {

	@Override
	public void onPreLaunch() {
		// this method does intentionally nothing.
		// It is required to get the mixin config plugin class loaded even early. And we want to get loaded as early as possible
	}

}
