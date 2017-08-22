/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
jQuery(function($) {
    // reset handler that clears the form
    $('form input:reset, form button:reset').click(function () {
        $(this).parents('form').each(function(){
        	$(this).find(':radio, :checkbox').removeAttr('checked');
        	$(this).find('textarea, :text, select').val('');
        });
        
        $(this).blur();
        
        return false;
    });
    
    $('form input.autofocus, form textarea.autofocus').each(function () {
	    $(this).focus(); 
	    if (this.setSelectionRange) {
		    var length= $(this).val().length;
		    this.setSelectionRange(length, length);
	    } else {
	    	 $(this).val($(this).val());
	    }
    });

    $('[data-toggle="tooltip"]').tooltip();
});