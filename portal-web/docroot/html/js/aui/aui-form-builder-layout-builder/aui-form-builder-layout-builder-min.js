YUI.add("aui-form-builder-layout-builder",function(e,t){var n=e.getClassName("form","builder","add","page","break"),r=e.getClassName("form","builder","add","row","collapsed"),i=e.getClassName("form","builder","choose","col","move"),s=e.getClassName("form","builder","choose","col","move","target"),o=e.getClassName("form","builder","field"),u=e.getClassName("form","builder","field","move","button"),a=e.getClassName("form","builder","field","move","target"),f=e.getClassName("form","builder","field","move","target","invalid"),l=e.getClassName("form","builder","field","moving"),c=e.getClassName("form","builder","layout"),h=e.getClassName("layout","builder","move","cancel"),p=e.getClassName("form","builder","layout","mode"),d=e.getClassName("form","builder","page","break","row","collapsed"),v=".layout-builder-add-row-choose-row";e.FormBuilderLayoutBuilder=function(){},e.FormBuilderLayoutBuilder.prototype={TITLE_LAYOUT:"Edit Layout",initializer:function(){this.after({layoutChange:this._afterLayoutBuilderLayoutChange,modeChange:this._afterLayoutBuilderModeChange,render:this._afterLayoutBuilderRender})},destructor:function(){this._layoutBuilder&&this._layoutBuilder.destroy()},_addColMoveButton:function(e,t){var n=e.all("."+u);n.setData("node-col",e),n.setData("node-row",t)},_addColMoveTarget:function(e){var t,n=e.get("node"),r;t=!e.get("movableContent")&&!n.one(".form-builder-empty-col");if(t)return;n.addClass(s),r=n.all("."+a),r.setData("col",e)},_afterLayoutBuilderIsColumnModeChange:function(){this._setPositionForPageBreakButton()},_afterLayoutBuilderLayoutChange:function(){this._layoutBuilder&&this._layoutBuilder.set("layout",this.get("layout"))},_afterLayoutBuilderModeChange:function(){var e=this.get("layout");this._uiSetLayoutBuilderMode(this.get("mode")),e.normalizeColsHeight(e.get("node").all(".row"))},_afterLayoutBuilderRender:function(){var t;this._layoutBuilder=new e.LayoutBuilder({addColMoveButton:e.bind(this._addColMoveButton,this),addColMoveTarget:e.bind(this._addColMoveTarget,this),clickColMoveTarget:e.bind(this._clickColMoveTarget,this),container:this.get("contentBox").one("."+c),layout:this.get("layout"),removeColMoveButtons:e.bind(this._removeColMoveButtons,this),removeColMoveTargets:e.bind(this._removeColMoveTargets,this)}),t=this._layoutBuilder.get("chooseColMoveTarget"),this._layoutBuilder.set("chooseColMoveTarget",e.bind(this._chooseColMoveTarget,this,t)),this._uiSetLayoutBuilderMode(this.get("mode")),this._layoutBuilder.get("layout").after("isColumnModeChange",e.bind(this._afterLayoutBuilderIsColumnModeChange,this)),this._setPositionForPageBreakButton(),this._eventHandles.push(this._fieldToolbar.on("onToolbarFieldMouseEnter",e.bind(this._onFormBuilderToolbarFieldMouseEnter,this))),this._removeLayoutCutColButtons()},_chooseColMoveTarget:function(e,t,n){var r=n.get("node"),i=t.ancestor("."+o),u;this._fieldBeingMoved=i.getData("field-instance"),this._fieldBeingMovedCol=n,r.addClass(s),i.addClass(l),u=i.previous("."+a),u&&u.addClass(f),u=i.next("."+a),u&&u.addClass(f),e(t,n)},_clickColMoveTarget:function(t){var n=this._fieldBeingMoved.get("content").ancestor("."+o),r=t.getData("nested-field-parent"),i=this._fieldToolbar.getItem("."+h);i&&i.removeClass(h),n?(n.getData("field-instance").removeNestedField(this._fieldBeingMoved),this.get("layout").normalizeColsHeight(new e.NodeList(this.getFieldRow(n.getData("field-instance"))))):this._fieldBeingMovedCol.set("value",null),r?this._addNestedField(r,this._fieldBeingMoved,t.getData("nested-field-index")):t.getData("col").set("value",this._fieldBeingMoved),this._layoutBuilder.cancelMove(),this._removeLayoutCutColButtons()},_collapseAddPageBreakButton:function(e,t){var n=t.one(v);e.addClass(d),n.addClass(r),t.append(e)},_enterLayoutMode:function(){this._layoutBuilder&&this._layoutBuilder.setAttrs({enableMoveCols:!1,enableMoveRows:!0,enableRemoveCols:!0,enableRemoveRows:!0}),this._updateHeaderTitle(this.TITLE_LAYOUT)},_exitLayoutMode:function(){this._layoutBuilder&&this._layoutBuilder.setAttrs({enableMoveCols:!0,enableMoveRows:!1,enableRemoveCols:!1,enableRemoveRows:!1}),this._updateHeaderTitle(this.TITLE_REGULAR)},_expandAddPageBreakButton:function(e,t){e.removeClass(d),t.append(e)},_onFormBuilderToolbarFieldMouseEnter:function(e){this._toggleMoveColItem(e.colNode)},_removeColMoveButtons:function(){this.get("contentBox").all("."+i).removeClass(i)},_removeColMoveTargets:function(){var e=this.get("contentBox");e.all("."+s).removeClass(s),e.all("."+l).removeClass(l),e.all("."+f).removeClass(f)},_removeLayoutCutColButtons:function(){this._layoutBuilder.get("removeColMoveButtons")()},_setPositionForPageBreakButton:function(){var e,t,r=this.get("contentBox"),i=this._layoutBuilder.get("layout").get("isColumnMode");e=r.one("."+n),t=this._layoutBuilder.addRowArea,i?this._collapseAddPageBreakButton(e,t):this._expandAddPageBreakButton(e,r)},_toggleMoveColItem:function(e){var t=e.getData("layout-col").get("movableContent"),n=this._fieldToolbar.getItem(".glyphicon-move").ancestor();t?(n.setData("layout-row",e.ancestor(".row").getData("layout-row")),n.setData("node-col",e),n.removeClass("hidden")):n.addClass("hidden")},_uiSetLayoutBuilderMode:function(t){t===e.FormBuilder.MODES.LAYOUT?(this._enterLayoutMode(),this.get("boundingBox").addClass(p)):(this._exitLayoutMode(),this.get("boundingBox").removeClass(p))}}},"3.0.1",{requires:["aui-classnamemanager","aui-layout-builder","base","node-base"],skinnable:!0});
