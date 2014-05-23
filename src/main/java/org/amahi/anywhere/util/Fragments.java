/*
 * Copyright (c) 2014 Amahi
 *
 * This file is part of Amahi.
 *
 * Amahi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Amahi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Amahi. If not, see <http ://www.gnu.org/licenses/>.
 */

package org.amahi.anywhere.util;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import org.amahi.anywhere.fragment.ServerImageFileFragment;
import org.amahi.anywhere.server.model.ServerFile;
import org.amahi.anywhere.server.model.ServerShare;

public final class Fragments
{
	private Fragments() {
	}

	public static final class Arguments
	{
		private Arguments() {
		}

		public static final String SERVER_FILE = "server_file";
		public static final String SERVER_SHARE = "server_share";
	}

	public static final class Builder
	{
		private Builder() {
		}

		public static Fragment buildServerFileFragment(ServerShare share, ServerFile file) {
			Fragment fileFragment = buildServerFileFragment(file);

			Bundle arguments = new Bundle();
			arguments.putParcelable(Arguments.SERVER_SHARE, share);
			arguments.putParcelable(Arguments.SERVER_FILE, file);

			fileFragment.setArguments(arguments);

			return fileFragment;
		}

		private static Fragment buildServerFileFragment(ServerFile file) {
			if (ServerImageFileFragment.FORMATS.contains(file.getMime())) {
				return new ServerImageFileFragment();
			}

			throw new RuntimeException();
		}
	}

	public static final class Operator
	{
		private final FragmentManager fragmentManager;

		public static Operator at(Activity activity) {
			return new Operator(activity);
		}

		private Operator(Activity activity) {
			this.fragmentManager = activity.getFragmentManager();
		}

		public void set(Fragment fragment, int fragmentContainerId) {
			if (isSet(fragmentContainerId)) {
				return;
			}

			fragmentManager
				.beginTransaction()
				.add(fragmentContainerId, fragment)
				.commit();
		}

		private boolean isSet(int fragmentContainerId) {
			return fragmentManager.findFragmentById(fragmentContainerId) != null;
		}

		public void replace(Fragment fragment, int fragmentConainerId) {
			fragmentManager
				.beginTransaction()
				.replace(fragmentConainerId, fragment)
				.commit();
		}

		public void replaceBackstacked(Fragment fragment, int fragmentConainerId) {
			fragmentManager
				.beginTransaction()
				.replace(fragmentConainerId, fragment)
				.addToBackStack(null)
				.commit();
		}

		public void removeBackstaced() {
			fragmentManager.popBackStack();
		}
	}
}