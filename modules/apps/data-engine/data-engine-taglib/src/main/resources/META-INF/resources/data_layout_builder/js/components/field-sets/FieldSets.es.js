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

import ClayButton from '@clayui/button';
import React, {useContext, useState} from 'react';

import AppContext from '../../AppContext.es';
import {dropFieldSet} from '../../actions.es';
import DataLayoutBuilder from '../../data-layout-builder/DataLayoutBuilder.es';
import DataLayoutBuilderContext from '../../data-layout-builder/DataLayoutBuilderContext.es';
import {DRAG_FIELDSET} from '../../drag-and-drop/dragTypes.es';
import {containsFieldSet} from '../../utils/dataDefinition.es';
import FieldType from '../field-types/FieldType.es';
import FieldSetModal from './FieldSetModal.es';

export default function FieldSets() {
	const [{appProps, dataDefinition, fieldSets}] = useContext(AppContext);
	const [fieldSetState, setFieldSetState] = useState({
		context: {},
		fieldSet: null,
		isVisible: false,
	});

	const toggleFieldSet = fieldSet => {
		let context = {};
		if (fieldSet) {
			const DataLayout = new DataLayoutBuilder({
				editingLanguageId: 'en_US',
				fieldTypes: window.App.fieldTypes,
			});

			const ctx = DataLayout.getDDMForm(fieldSet);
			const pages = ctx.pages;

			delete ctx.pages;

			context = {
				...window.App.context,
				pages: [
					{
						...ctx,
						description: '',
						rows: pages[0].rows,
						title: '',
					},
				],
			};
		}

		setFieldSetState({
			context,
			fieldSet,
			isVisible: !fieldSetState.isVisible,
		});
	};

	const [dataLayoutBuilder] = useContext(DataLayoutBuilderContext);

	const onDoubleClick = ({fieldSet: {name: fieldName}, fieldSet}) => {
		const {activePage, pages} = dataLayoutBuilder.getStore();

		dataLayoutBuilder.dispatch(
			'fieldSetAdded',
			dropFieldSet({
				dataLayoutBuilder,
				fieldName,
				fieldSet,
				indexes: {
					columnIndex: 0,
					pageIndex: activePage,
					rowIndex: pages[activePage].rows.length,
				},
			})
		);
	};

	return (
		<>
			<ClayButton
				block
				className="add-fieldset"
				displayType="secondary"
				onClick={() => toggleFieldSet()}
			>
				{Liferay.Language.get('add-fieldset')}
			</ClayButton>

			<div className="mt-3">
				{fieldSets.map(fieldSet => {
					const dropDownActions = [
						{
							action: () => toggleFieldSet(fieldSet),
							name: Liferay.Language.get('edit'),
						},
						{
							action: () => {
								const confirmed = confirm(
									Liferay.Language.get(
										'are-you-sure-you-want-to-delete-this'
									)
								);

								if (confirmed) {
									alert('Removeu');
								}
							},
							name: Liferay.Language.get('delete'),
						},
					];

					return (
						<FieldType
							actions={dropDownActions}
							description={`${
								fieldSet.dataDefinitionFields.length
							} ${Liferay.Language.get('fields')}`}
							disabled={
								fieldSet.disabled ||
								containsFieldSet(dataDefinition, fieldSet.id)
							}
							dragType={DRAG_FIELDSET}
							fieldSet={fieldSet}
							icon="forms"
							key={fieldSet.dataDefinitionKey}
							label={fieldSet.name[themeDisplay.getLanguageId()]}
							onDoubleClick={onDoubleClick}
						/>
					);
				})}
			</div>

			<FieldSetModal
				caller="Fieldset"
				context={fieldSetState.context}
				fieldSet={fieldSetState.fieldSet}
				isVisible={fieldSetState.isVisible}
				onClose={() => toggleFieldSet()}
			/>
		</>
	);
}
