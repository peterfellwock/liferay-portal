/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

import {ClayCheckbox} from '@clayui/form';
import ClayIcon from '@clayui/icon';
import React, {useEffect, useState} from 'react';

import {FieldBase} from '../FieldBase/ReactFieldBase.es';
import {useSyncValue} from '../hooks/useSyncValue.es';

const Switcher = ({
	checked: initialChecked,
	disabled,
	label,
	name,
	onChange,
	required,
	showLabel,
	showMaximumRepetitionsInfo,
	systemSettingsURL,
	visible,
}) => {
	const [checked, setChecked] = useSyncValue(initialChecked, true);

	useEffect(() => {
		setChecked(initialChecked);
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [disabled, visible]);

	return (
		<>
			<label className="ddm-toggle-switch toggle-switch">
				<input
					checked={checked}
					className="toggle-switch-check"
					disabled={disabled}
					name={name}
					onChange={(event) => {
						setChecked(event.target.checked);
						onChange(event, event.target.checked);
					}}
					type="checkbox"
					value={true}
				/>

				<span aria-hidden="true" className="toggle-switch-bar">
					<span className="toggle-switch-handle"></span>

					{(showLabel || required) && (
						<span className="toggle-switch-text toggle-switch-text-right">
							{showLabel && label}

							{required && (
								<span className="ddm-label-required reference-mark">
									<ClayIcon symbol="asterisk" />
								</span>
							)}
						</span>
					)}
				</span>
			</label>
			{checked && showMaximumRepetitionsInfo && (
				<div className="ddm-info">
					<span className="ddm-tooltip">
						<ClayIcon symbol="info-circle" />
					</span>
					<div
						className="ddm-info-text"
						dangerouslySetInnerHTML={{
							__html: Liferay.Util.sub(
								Liferay.Language.get(
									'for-security-reasons-upload-field-repeatability-is-limited-the-limit-is-defined-in-x-system-settings-x'
								),
								`<a href=${systemSettingsURL} target="_blank">`,
								'</a>'
							),
						}}
					/>
				</div>
			)}
		</>
	);
};

const Checkbox = ({
	checked: initialChecked,
	disabled,
	label,
	name,
	onChange,
	required,
	showLabel,
}) => {
	const [checked, setChecked] = useState(initialChecked);

	return (
		<ClayCheckbox
			checked={checked}
			disabled={disabled}
			label={showLabel && label}
			name={name}
			onChange={(event) => {
				setChecked(event.target.checked);
				onChange(event, event.target.checked);
			}}
		>
			{showLabel && required && (
				<span className="ddm-label-required reference-mark">
					<ClayIcon symbol="asterisk" />
				</span>
			)}
		</ClayCheckbox>
	);
};

const Main = ({
	label,
	name,
	onChange,
	predefinedValue = true,
	readOnly,
	required,
	showAsSwitcher = true,
	showLabel = true,
	showMaximumRepetitionsInfo = false,
	spritemap,
	systemSettingsURL,
	value,
	visible,
	...otherProps
}) => {
	const Toggle = showAsSwitcher ? Switcher : Checkbox;

	return (
		<FieldBase
			name={name}
			showLabel={false}
			visible={visible}
			{...otherProps}
		>
			<Toggle
				checked={
					!!(
						value ??
						(predefinedValue.length
							? predefinedValue[0] === 'true'
							: predefinedValue)
					)
				}
				disabled={readOnly}
				label={label}
				name={name}
				onChange={onChange}
				required={required}
				showLabel={showLabel}
				showMaximumRepetitionsInfo={showMaximumRepetitionsInfo}
				spritemap={spritemap}
				systemSettingsURL={systemSettingsURL}
				visible={visible}
			/>
		</FieldBase>
	);
};

Main.displayName = 'Checkbox';

export {Main};
export default Main;
