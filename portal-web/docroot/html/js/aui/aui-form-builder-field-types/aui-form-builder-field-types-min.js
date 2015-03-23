YUI.add("aui-form-builder-field-types",function(e,t){var n=e.getClassName("field","type"),r=e.getClassName("form","builder","field","types","list");e.FormBuilderFieldTypes=function(){},e.FormBuilderFieldTypes.prototype={initializer:function(){this.after("fieldTypesChange",this._afterFieldTypesChange)},destructor:function(){e.Array.each(this.get("fieldTypes"),function(e){e.destroy()}),this._fieldTypesModal&&this._fieldTypesModal.destroy()},disableUniqueFieldType:function(e){var t=this.findTypeOfField(e);t.get("unique")&&t.set("disabled",!0)},findTypeOfField:function(e){var t=this.get("fieldTypes"),n;for(n=0;n<t.length;n++)if(e.constructor===t[n].get("fieldClass"))return t[n]},hideFieldsPanel:function(){this._fieldTypesModal&&this._fieldTypesModal.hide()},registerFieldTypes:function(t){var n=this.get("fieldTypes");t=e.Lang.isArray(t)?t:[t],e.Array.each(t,function(e){n.push(e)}),this.set("fieldTypes",n)},showFieldsPanel:function(){this._fieldTypesPanel||this._createFieldTypesPanel(),this._fieldTypesModal.show()},unregisterFieldTypes:function(t){var n=this;t=e.Lang.isArray(t)?t:[t],e.Array.each(t,function(e){n._unregisterFieldType(e)}),this.set("fieldTypes",this.get("fieldTypes"))},_afterFieldTypesChange:function(){this._uiSetFieldTypes(this.get("fieldTypes"))},_bindFieldTypesModalEvents:function(){this._eventHandles.push(this._fieldTypesPanel.delegate("click",this._onClickFieldType,"."+n,this),this._fieldTypesPanel.delegate("key",e.bind(this._onKeyPressFieldType,this),"enter","."+n))},_buildFieldTypesToolbarConfig:function(){return{header:[{cssClass:"close",label:"\u00d7",on:{click:e.bind(this._onFieldTypesModalCloseClick,this)}}]}},_createFieldTypesPanel:function(){this._fieldTypesPanel=e.Node.create('<div class="clearfix '+r+'" role="main" />'),this._fieldTypesModal=(new e.Modal({bodyContent:this._fieldTypesPanel,centered:!0,cssClass:"form-builder-modal",draggable:!1,headerContent:"Add Field",modal:!0,resizable:!1,toolbars:this._buildFieldTypesToolbarConfig(),visible:!1,zIndex:2})).render(),this._uiSetFieldTypes(this.get("fieldTypes")),this._bindFieldTypesModalEvents()},_hasFieldType:function(e,t){var n,r=t.get("nestedFields");if(t.constructor===e.get("fieldClass"))return!0;for(n=0;n<r.length;n++)if(this._hasFieldType(e,r[n]))return!0;return!1},_hasFieldTypeAll:function(t){var n,r,i,s,o=this.get("layout").get("rows");for(s=0;s<o.length;s++){r=o[s].get("cols");for(n=0;n<r.length;n++){i=r[n].get("value");if(i&&i instanceof e.FormField&&this._hasFieldType(t,i))return!0}}return!1},_onClickFieldType:function(e){var t,n=e.currentTarget.getData("fieldType");n.get("disabled")||(this.hideFieldsPanel(),t=new(n.get("fieldClass"))(n.get("defaultConfig")),this.showFieldSettingsPanel(t,n.get("label")))},_onFieldTypesModalCloseClick:function(){this.hideFieldsPanel(),this._newFieldContainer=null},_onKeyPressFieldType:function(e){this._onClickFieldType(e)},_setFieldTypes:function(t){for(var n=0;n<t.length;n++)e.instanceOf(t[n],e.FormBuilderFieldType)||(t[n]=new e.FormBuilderFieldType(t[n]));return t},_uiSetFieldTypes:function(t){var n=this;if(!this._fieldTypesPanel)return;this._fieldTypesPanel.get("children").remove(),e.Array.each(t,function(e){n._fieldTypesPanel.append(e.get("node"))})},_unregisterFieldType:function(t){var n=this.get("fieldTypes"),r;if(e.Lang.isFunction(t))for(r=n.length-1;r>=0;r--)n[r].get("fieldClass")===t&&this._unregisterFieldTypeByIndex(r);else this._unregisterFieldTypeByIndex(n.indexOf(t))},_unregisterFieldTypeByIndex:function(e){var t=this.get("fieldTypes");e!==-1&&(t[e].destroy(),t.splice(e,1))},_updateUniqueFieldType:function(){var t=this;e.Array.each(t.get("fieldTypes"),function(e){e.get("unique")&&e.set("disabled",t._hasFieldTypeAll(e))})}},e.FormBuilderFieldTypes.ATTRS={fieldTypes:{setter:"_setFieldTypes",validator:e.Lang.isArray,value:[]}}},"3.0.1",{requires:["aui-classnamemanager","aui-modal","base","node-base"],skinnable:!0});
