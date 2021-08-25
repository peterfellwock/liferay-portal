/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import ClayDropDown from '@clayui/drop-down';
import React, {useContext} from 'react';

import ChartContext from '../ChartContext';
import {deleteAccount} from '../data/accounts';
import {PERMISSION_CHECK_ON_HEADLESS_API_ACTIONS} from '../utils/flags';

export default function AccountMenuContent({closeMenu, data, parentData}) {
	const {chartInstanceRef} = useContext(ChartContext);

	function handleDelete() {
		if (
			confirm(
				Liferay.Util.sub(
					Liferay.Language.get('x-will-be-deleted'),
					data.name
				)
			)
		) {
			deleteAccount(data.id).then(() => {
				chartInstanceRef.current.deleteNodes([data], true);

				if (parentData) {
					chartInstanceRef.current.updateNodeContent({
						...parentData,
						numberOfAccounts: parentData.numberOfAccounts - 1,
					});
				}

				closeMenu();
			});
		}
	}

	const actions = [];

	if (!PERMISSION_CHECK_ON_HEADLESS_API_ACTIONS) {
		actions.push(
			<ClayDropDown.Item key="delete" onClick={handleDelete}>
				{Liferay.Language.get('delete')}
			</ClayDropDown.Item>
		);
	}

	return actions;
}
