var easyLaw = angular.module('easyLaw', [ 'ngSanitize', 'ui', 'ngResource' ]);

easyLaw.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/login', {
		templateUrl : baseUrl + '/resources/templates/login.html',
		controller : LoginCtrl
	}).when('/signup', {
		templateUrl : baseUrl + '/resources/templates/signup.html',
		controller : SignupCtrl
	}).when('/signup/:asLawyer', {
		templateUrl : baseUrl + '/resources/templates/signup.html',
		controller : SignupCtrl
	}).when('/lawyer/profile', {
		templateUrl : baseUrl + '/resources/templates/profile.html',
		controller : ProfileCtrl
	}).when('/lawyer/edit', {
		templateUrl : baseUrl + '/resources/templates/edit.html',
		controller : EditCtrl
	}).when('/lawyer/questions', {
		templateUrl : baseUrl + '/resources/templates/lawyerquestions.html',
		controller : LawyerQuestionsCtrl
	}).when('/lawyer/questions/:questionId', {
		templateUrl : baseUrl + '/resources/templates/lawyerquestions.html',
		controller : LawyerQuestionsCtrl
	}).when('/lawyer/settings', {
		templateUrl : baseUrl + '/resources/templates/lawyersettings.html',
		controller : LawyerSettingsCtrl
	}).when('/lawyer/files', {
		templateUrl : baseUrl + '/resources/templates/customerfiles.html',
		controller : CustomerFilesCtrl
	}).when('/lawyer/files/:fileId', {
		templateUrl : baseUrl + '/resources/templates/customerfiles.html',
		controller : CustomerFilesCtrl
	}).when('/lawyers', {
		templateUrl : baseUrl + '/resources/templates/lawyers.html',
		controller : LawyersCtrl
	}).when('/lawyers/:username', {
		templateUrl : baseUrl + '/resources/templates/profile.html',
		controller : ProfileCtrl
	}).when('/terms', {
		templateUrl : baseUrl + '/resources/templates/terms.html'
	}).when('/privacy', {
		templateUrl : baseUrl + '/resources/templates/privacy.html'
	}).when('/articles', {
		templateUrl : baseUrl + '/resources/templates/articles.html',
		controller : ArticlesCtrl
	}).when('/articles/:title', {
		templateUrl : baseUrl + '/resources/templates/articles.html',
		controller : ArticlesCtrl
	}).when('/question/ask', {
		templateUrl : baseUrl + '/resources/templates/askquestionform.html',
		controller : QuestionCtrl
	}).when('/customer/questions', {
		templateUrl : baseUrl + '/resources/templates/customerquestions.html',
		controller : CustomerQuestionsCtrl
	}).when('/customer/questions/:questionId', {
		templateUrl : baseUrl + '/resources/templates/customerquestions.html',
		controller : CustomerQuestionsCtrl
	}).when('/customer/settings', {
		templateUrl : baseUrl + '/resources/templates/customersettings.html',
		controller : CustomerSettingsCtrl
	}).when('/customer/book/:quoteId', {
		templateUrl : baseUrl + '/resources/templates/book.html',
		controller : BookCtrl
	}).when('/customer/files', {
		templateUrl : baseUrl + '/resources/templates/customerfiles.html',
		controller : CustomerFilesCtrl
	}).when('/customer/files/:fileId', {
		templateUrl : baseUrl + '/resources/templates/customerfiles.html',
		controller : CustomerFilesCtrl
	}).when('/aboutus', {
		templateUrl : baseUrl + '/resources/templates/aboutus.html'
	}).when('/contactus', {
		templateUrl : baseUrl + '/resources/templates/contactus.html'
	}).when('/lawyersjoinus', {
		templateUrl : baseUrl + '/resources/templates/lawyersjoinus.html'
	}).when('/', {
		templateUrl : baseUrl + '/resources/templates/home.html',
		controller : HomeCtrl
	}).otherwise({
		redirectTo : '/'
	});
} ]);

easyLaw.config([ '$locationProvider', function($location) {
	$location.hashPrefix('!');
} ]);

easyLaw.factory('sharedService', function($rootScope) {
	var sharedService = {};

	sharedService.items = new Array();

	sharedService.broadcastItem = function(item, event) {
		this.items[event] = item;
		$rootScope.$broadcast(event);
	};

	return sharedService;
});

easyLaw.directive('easyScroller', function() {
	return function(scope, element, attrs) {

		var currentIndex = 0;

		scope.$watch(attrs.easyScroller, function(value) {
			currentIndex = value;
			scroll();
		});

		function scroll() {
			var rootItem = $(element);
			var scrollables = rootItem.find("div.scrollable");
			var scrollableToShow;
			for ( var i = 0; i < scrollables.length; ++i) {
				var scrollable = $(scrollables[i]);
				if (i == currentIndex) {
					scrollableToShow = scrollable;
				} else {
					scrollable.css('opacity', 0.0)
				}
			}
			scrollableToShow.animate({
				opacity : 1.0,
			}, 500, function() {
				// Animation complete.
			});
		}
	};
});

easyLaw.directive('radioToggle', function() {
	return function(scope, element, attrs) {

		scope.$watch(attrs.radioToggle, function(value) {
			var radio = $(element);
			if (radio.hasClass('checked')) {
				radio.removeClass('checked');
			} else {
				radio.addClass('checked');
			}
		});
	};
});

easyLaw.filter('timeelapsed', function() {
	return function(input) {

		var output = '';

		var now = new Date();
		var prev = new Date(input);

		var yrDif = Math.abs(Date.DateDiff('yyyy', now.toString(), prev
				.toString(), 2));
		var monDif = Math.abs(Date.DateDiff('m', now.toString(), prev
				.toString(), 2));

		if (yrDif > 0) {
			output += yrDif + ' year';
			if (yrDif > 1)
				output += "s ";
			else
				output += " ";
			monDif -= (yrDif * 12);
		}
		if (monDif > 0) {
			output += monDif + ' month';
			if (monDif > 1)
				output += "s ";
			else
				output += " ";
		}
		if (yrDif <= 0 && monDif <= 0) {
			var dayDif = Math.abs(Date.DateDiff('d', now.toString(), prev
					.toString(), 2));
			if (dayDif > 0) {
				output += dayDif + ' day';
				if (dayDif > 1)
					output += "s ";
				else
					output += " ";
			} else {
				var hourDif = Math.abs(Date.DateDiff('h', now.toString(), prev
						.toString(), 2));
				if (hourDif > 0) {
					if (dayDif > 0)
						hourDif -= dayDif * 24;
					output += hourDif + ' hour';
					if (hourDif > 1)
						output += "s ";
					else
						output += " ";
				} else {
					var minDif = Math.abs(Date.DateDiff('n', now.toString(),
							prev.toString(), 2));
					if (minDif > 0) {
						output += minDif + ' min ';
					} else {
						var secDif = Math.abs(Date.DateDiff('s',
								now.toString(), prev.toString(), 2));
						if (minDif > 0)
							secDif -= 60 * minDif;
						if (secDif > 0) {
							output += secDif + " second";
							if (secDif > 1)
								output += "s ";
							else
								output += " ";
						}
					}
				}
			}
		}
		if (!output || output.length <= 0)
			return "now";
		else
			return output;
	};
});

easyLaw.directive('editable', function() {
	return function(scope, element, attrs) {

		$(element).css('cursor', 'pointer');
		var original = $($(element).find('.original')[0]);
		var edit = $($(element).find('.edit')[0]);

		$(element).click(function() {
			originalValue = attrs.editable;
			if (original.is(":visible")) {
				original.hide();
				edit.show();
			}
		});
		if (!edit.hasClass('no-enter')) {
			edit.keypress(function(e) {
				if (e.which == 13) {
					if (edit.val() && edit.val().trim().length > 0) {
						scope.edit(close);
					}
				}
			});
		}
		edit.focusout(function() {
			if (edit.val() && edit.val().trim().length > 0) {
				scope.edit(close);
			}
		});

		function close() {
			if (edit.is(":visible")) {
				edit.hide();
				original.show();
			}
		}
	};
});

easyLaw.filter('newlines', function() {
	return function(text) {
		if (text)
			return text.replace(/\n/g, '<br/>');
		else
			return undefined;
	};
});

easyLaw.filter('noHTML', function() {
	return function(text) {
		if (text) {
			return text.replace(/&/g, '&amp;').replace(/>/g, '&gt;').replace(
					/</g, '&lt;');
		} else
			return undefined;
	}
});

easyLaw.filter('blankWithCurrent', function() {
	return function(text) {
		if (!text || text.length <= 0)
			return 'now';
		else
			return text;
	};
});

easyLaw.directive('popup', function($window) {
	return function(scope, element, attrs) {

		var popup = false;
		var popupElement = $(element).parent().find('.popup');

		$(element).click(function() {
			if (popupElement) {
				if (popup)
					$(popupElement).hide();
				else
					$(popupElement).show();
			}
		});

		var w = angular.element($window);

		$(window).resize(function() {
			onresize();
		});

		function onresize() {
			if (popupElement) {
				var windowHeight = w.height();
				var elementHeight = $(popupElement).height();
				var remainder = windowHeight - elementHeight;
				if (remainder > 0) {
					var top = remainder / 2;
					$(popupElement).css('top', top);
				} else
					$(popupElement).css('top', 0);
				var windowWidth = w.width();
				var elementWidth = $(popupElement).width();
				remainder = windowWidth - elementWidth;
				if (remainder > 0) {
					var left = remainder / 2;
					$(popupElement).css('left', left);
				} else
					$(popupElement).css('left', 0);
			}
		}

		onresize();
	};
});

easyLaw.directive('popupResize', function($window) {
	return function(scope, element, attrs) {

		var popup = false;
		var popupElement = $(element).parent().find('.popup');

		var w = angular.element($window);

		$(window).resize(function() {
			onresize();
		});

		function onresize() {
			if (popupElement) {
				var windowHeight = w.height();
				var elementHeight = $(popupElement).height();
				var remainder = windowHeight - elementHeight;
				if (remainder > 0) {
					var top = remainder / 2;
					$(popupElement).css('top', top);
				} else
					$(popupElement).css('top', 0);
				var windowWidth = w.width();
				var elementWidth = $(popupElement).width();
				remainder = windowWidth - elementWidth;
				if (remainder > 0) {
					var left = remainder / 2;
					$(popupElement).css('left', left);
				} else
					$(popupElement).css('left', 0);
			}
		}

		onresize();
	};
});

easyLaw.directive('close', function($window) {
	return function(scope, element, attrs) {
		$(element).click(function() {
			$(element).parent().hide();
		});
	};
});

easyLaw.directive('fineuploader', function($window) {
	return function(scope, element, attrs) {

		var type = attrs.fineuploader;
		var fineclose = attrs.fineclose;

		$(element).fineUploader({
			request : {
				endpoint : baseUrl + '/lawyer/photo/' + type
			}
		}).on('error', function(event, id, name, reason) {
			// do something
		}).on('complete', function(event, id, name, responseJSON) {
			scope.load();
			if (fineclose) {
				$(element).parent().hide();
			}
		});
	};
});

easyLaw.directive('resizableImage', function() {
	return function(scope, element, attrs) {

		scope.$watch(attrs.resizableImage, function(value) {

			if (value && value.id) {

				var url = baseUrl + "/photo/" + value.id;
				$(element).css('background-image', 'url(' + url + ')');
				$(element).css('cursor', 'pointer');

				setPosition();
				var moving = false;
				var startX = null;
				var startY = null;

				if (scope.isLawyer) {

					$(element).unbind('mousedown');
					$(element).unbind('mouseup');
					$(element).unbind('mouseleave');
					$(element).unbind('mousemove');

					$(element).mousedown(function(event) {
						startMove(event);
					});
					$(element).mouseup(function() {
						stopMove();
					});
					$(element).mouseleave(function() {
						stopMove();
					});

					$(element).mousemove(function(event) {
						if (moving) {
							movePosition(event);
						}
					});
				}
			}

			function startMove(event) {

				startX = event.pageX;
				startY = event.pageY;
				moving = true;
				$(element).css('cursor', 'move');
			}

			function stopMove() {
				$(element).css('cursor', 'pointer');
				moving = false;
			}

			function movePosition(event) {

				var x = event.pageX;
				var y = event.pageY;

				var xDif = x - startX;
				var yDif = y - startY;

				if (attrs.resizeX)
					value.positionX = value.positionX + xDif;
				if (attrs.resizeY)
					value.positionY = value.positionY + yDif;

				setPosition();

				startX = x;
				startY = y;

				scope.$apply('modified = true');

				// scope.setBackgroundPhotoPosition(value.positionX,value.positionY);

				// console.log(backgroundPosition);
			}

			function setPosition() {
				var backgroundPosition = value.positionX + "px "
						+ value.positionY + "px";
				$(element).css('background-position', backgroundPosition);
			}

		});
	};
});

easyLaw.directive('backgroundPhoto', function() {
	return function(scope, element, attrs) {

		scope.$watch(attrs.backgroundPhoto, function(value) {
			if (value) {
				$(element).css('height', '320px');
				var otherPhoto = $(element).parent().find('.photo');
				$(otherPhoto).css('top', '234px');
			}
		});
	};
});

easyLaw.directive('navSelect', function() {
	return function(scope, element, attrs) {

		$(element).find('ul li').each(function(index, value) {
			$(value).click(function() {
				$(element).find('ul li').each(function(index2, value2) {
					$(value2).removeClass('selected');
				});
				$(value).addClass('selected');
			});
		});
	};
});

easyLaw.directive('selectable', function() {
	return function(scope, element, attrs) {
		$(element).parent().find('li').each(function(index, value) {
			$(value).click(function() {
				$(element).parent().find('li').each(function(index2, value2) {
					$(value2).removeClass('selected');
				});
				$(value).addClass('selected');
			});
		});
	};
});

easyLaw.directive('linkSelect', function() {
	return function(scope, element, attrs) {

		$(element).find('a').each(function(index, value) {
			$(value).click(function() {
				$(element).find('a').each(function(index2, value2) {
					$(value2).removeClass('selected');
				});
				$(value).addClass('selected');
			});
		});
	};
});

var currentId = 0;

easyLaw.directive('ckeditor', function($window) {
	return function(scope, element, attrs) {

		var id = 'id-' + currentId++;
		$(element).attr('id', id);
		var editor = CKEDITOR.replace(id, {
			allowedContent : {
				'p h1 h2 h3 h4 h5 ul ol li table tr td' : {
					styles : 'text-align'
				},
				a : {
					attributes : '!href'
				},
				'strong em b' : true
			}
		});
		scope.setEditor(editor);

		scope.$watch('showPopupValue', function(value) {

			var popup = $(element).parent().parent();
			if (!popup.hasClass('popup'))
				popup = $(element).parent().parent().parent();

			if (value)
				popup.show();
			else
				popup.hide();
		});

	};
});

easyLaw.directive('writeResponse', function() {
	return function(scope, element, attrs) {

		var writeAResponse = $(element).find('.write-a-response');
		var responseForm = $(element).find('.response-form');

		scope.$watch('whatToShow', function(value) {

			if (value == 'response-form') {
				$($(element).find('.response-form textarea:first')).focus();
				$(element).parent().parent().addClass('selected');
			} else if (value == 'write-a-response') {
				$(element).parent().parent().removeClass('selected');
			} else if (value == 'answered') {
				$(element).parent().parent().removeClass('selected');
			} else if (value == 'edit-response') {
				$($(element).find('.edit-response-form textarea:first'))
						.focus();
				$(element).parent().parent().addClass('selected');
			}
		});
	};
});

easyLaw.directive('selectNav', function() {
	return function(scope, element, attrs) {

		scope.$watch('selectNavValue', function(value) {
			if (value) {
				$(element).find('ul li').each(function(index, value) {
					$(value).removeClass('selected');
				});
				var chosen = $(element).find('ul li.' + value);
				$(chosen).addClass('selected');
			}
		});
	};
});

easyLaw.directive('clickToSelectQuestion', function() {
	return function(scope, element, attrs) {

		$(element).click(
				function() {
					$(element).parent().find('div.question').each(
							function(index, value) {
								$(value).removeClass('selected');
							});
					$(element).addClass('selected');
				});
	};
});

/*
 * easyLaw.directive('fullscreen', function() { return function(scope, element,
 * attrs) {
 * 
 * function layout() { var header = $('header'); var headerHeight =
 * header.height(); headerHeight += parseInt(header.css('padding-top'));
 * headerHeight += parseInt(header.css('padding-bottom'));
 * 
 * var footer = $('footer'); var footerHeight = footer.height(); footerHeight +=
 * parseInt(footer.css('padding-top')); footerHeight +=
 * parseInt(footer.css('padding-bottom'));
 * 
 * var subheadingHeight = 44;
 * 
 * var paddingTop = parseInt($(element).css('padding-top')); var paddingBottom =
 * parseInt($(element).css('padding-bottom')); var padding = paddingTop +
 * paddingBottom;
 * 
 * var extraBottom = 15;
 * 
 * var windowHeight = $(window).height(); var remainder = windowHeight -
 * headerHeight - footerHeight - subheadingHeight - padding - extraBottom;
 * 
 * 
 * $('.list').css('top',headerHeight + subheadingHeight);
 * $('.list').css('bottom',footerHeight+extraBottom);
 * 
 * $('.file-content').css('top',headerHeight + subheadingHeight);
 * $('.file-content').css('bottom',footerHeight+extraBottom);
 * 
 * var width = $(window).width(); var percentageInPixels = (width/100)*5;
 * 
 * $('.list').css('left',percentageInPixels);
 * $('.file-content').css('left',309+percentageInPixels);
 * $('.file-content').css('right',percentageInPixels);
 * 
 * scope.scrollPaneAll(); }; if( !isMobile() ) { $(window).resize(function() {
 * layout(); }); layout(); } }; });
 */

easyLaw.directive('autocomplete', function($http) {
	return function(scope, element, attrs) {

		var input = $(element);

		var lastCaretPosition = 0;
		var lastText = "";

		var currentIndex = 0;
		var elements = new Array();

		input.keydown(function(e) {

			var keyCode = e.keyCode || e.which;
			// console.log(keyCode);
			if (keyCode == 37 || keyCode == 39) {
			} // do nothing, left and right
			else if (keyCode == 9) {
				closeAutoComplete();
			} else if (keyCode == 13) {
				closeAutoComplete();
			} else if (keyCode == 38) {
				previousValue();
			} else if (keyCode == 40) {
				nextValue();
			} else {
				setTimeout(function() {
					var text = input.val();
					var currentCaretPosition = input.caret().start;
					var searchText = text.substring(0, currentCaretPosition);
					if (searchText && searchText.length > 0)
						autocomplete(searchText);
					else {
						input.val("");
						lastCaretPosition = 0;
						lastText = "";
					}
				}, 50);
			}
		});

		function autocomplete(query) {
			var url = baseUrl + '/autocomplete/suburb?query=' + query;
			$http.get(url).success(function(suburbs) {
				if (suburbs && suburbs.length > 0) {
					setTimeout(function() {
						var currentCaretPosition = input.caret().start;
						updateScopeValue(suburbs[0]);
						input.caret({
							start : currentCaretPosition,
							end : currentCaretPosition
						});
						lastCaretPosition = currentCaretPosition;
						lastText = suburbs[0];

						updateBox(suburbs);
					}, 50);
				} else {
					setTimeout(function() {
						updateScopeValue(lastText);
						input.caret({
							start : lastCaretPosition,
							end : lastCaretPosition
						});
					}, 50);
				}
			});
		}

		function updateBox(suburbs) {
			if (suburbs.length > 1) {
				var html = "<ul class='autocomplete'>";
				for (i = 0; i < suburbs.length; ++i) {
					html += "<li>" + suburbs[i] + "</li>";
				}
				html += "</ul>";
				closeAutoComplete();
				input.after(html);
				var padding = input.css('padding-left');
				elements = input.next().find('li');
				elements.each(function(index, li) {
					if (index == 0)
						$(li).addClass('selected');
					var content = $(li).html();
					$(li).css('padding-left', padding);
					$(li).css('padding-right', padding);
					$(li).click(function(event) {
						$(li).addClass('selected');
						setNewValue(content);
						closeAutoComplete();
					});
				});
			} else {
				closeAutoComplete();
			}

			currentIndex = 0;
		}

		function closeAutoComplete() {
			if (input.next().hasClass('autocomplete'))
				input.next().remove();
		}

		function setNewValue(value) {
			updateScopeValue(value);
			lastCaretPosition = input.caret().start;
			lastText = value;
		}

		function previousValue() {
			var newIndex = currentIndex - 1;
			if (newIndex < 0)
				newIndex = 0;
			navigate(newIndex);
		}

		function nextValue() {
			var newIndex = currentIndex + 1;
			navigate(newIndex);
		}

		function navigate(newIndex) {
			if (autocomplete) {
				if (newIndex < elements.length) {
					$(elements.get(currentIndex)).removeClass('selected');
					var newElement = $(elements.get(newIndex));
					newElement.addClass('selected');
					currentIndex = newIndex;
					setNewValue(newElement.html());
					input.next().scrollTo(newElement);
				}
			}
		}

		function updateScopeValue(value) {
			input.val(value);
			if (scope.setAutocompleteValue)
				scope.setAutocompleteValue(value);
		}

	};
});

easyLaw.directive('slider', function factory() {
	return {
		require : '?ngModel',
		link : function(scope, element, attrs, ngModel) {

			if (!ngModel)
				return;

			ngModel.$render = function() {
				var value = ngModel.$viewValue;
				if (!value)
					value = 5;
				if (value) {
					var valueElement = $($(element).parent().find(
							'.rating-value'));
					valueElement.html(value);
					if (value >= 5)
						valueElement.css('color', 'green');
					else
						valueElement.css('color', 'red');
					$(element).slider('value', value * 100);
				}
			};

			var currentValue = 5;

			$(element).slider({
				animate : "fast",
				value : 500,
				min : 100,
				max : 1000,
				slide : function(event, ui) {

					currentValue = Math.round(ui.value / 100);
					scope.$apply(read);
				}
			});

			function read() {
				ngModel.$setViewValue(currentValue);
				ngModel.$render();
			}
		}
	};
});

easyLaw.directive('tooltip', function() {
	return function(scope, element, attrs) {

		var tooltip = $('.tooltip');
		var text = $('.tooltip .text');

		function showTooltip() {
			text.html(attrs.tooltip);

			var offset = $(element).offset();
			var width = $(element).width();
			var height = $(element).height();
			var paddingLeft = parseInt($(element).css('padding-left'));
			var paddingRight = parseInt($(element).css('padding-right'));

			var top = offset.top;
			var left = offset.left + width + paddingLeft + paddingRight + 20;

			var windowWidth = $(window).width();
			var windowHeight = $(window).height();

			if (windowWidth >= 500) {
				tooltip.css('top', top);
				tooltip.css('left', left);
			} else { // small devices
				tooltip.css('bottom', windowHeight - offset.top + 10);
				tooltip.css('left', 10);
				tooltip.css('right', 10);
				tooltip.css('margin-right', 0);
			}

			tooltip.show();

			var textHeight = text.height();
			$('.tooltip .image').css('height', textHeight + 20); // 20 for
																	// padding
		}

		function hideTooltip() {
			text.html('');
			tooltip.hide();
		}

		$(element).hover(function() {
			showTooltip();
		}, function() {
			hideTooltip();
		});

		$(element).focus(function() {
			showTooltip();
		});

		$(element).blur(function() {
			hideTooltip();
		});

	};
});

easyLaw.filter('escape', function() {
	return encodeURIComponent;
});

easyLaw.filter('unescape', function() {
	return decodeURIComponent;
});

clone = function(object) {
	var newObj = (object instanceof Array) ? [] : {};
	for (i in object) {
		newObj[i] = object[i];
	}
	return newObj;
};

getDocHeight = function() {
	var D = document;
	return Math.max(Math.max(D.body.scrollHeight,
			D.documentElement.scrollHeight), Math.max(D.body.offsetHeight,
			D.documentElement.offsetHeight), Math.max(D.body.clientHeight,
			D.documentElement.clientHeight));
};

easyLaw.factory('User', function($resource) {
	var nocache = new Date().getTime();
	return $resource(baseUrl + '/user?nocache=' + nocache, {}, {
		query : {
			method : 'GET',
			isArray : false
		}
	});
});

easyLaw.factory('Lawyer', function($resource) {
	var nocache = new Date().getTime();
	return $resource(baseUrl + '/lawyer/profile?nocache=' + nocache, {}, {
		query : {
			method : 'GET',
			isArray : false
		},
		update : {
			method : 'PUT',
			isArray : false
		}
	});
});

easyLaw.factory('Customer', function($resource) {
	var nocache = new Date().getTime();
	return $resource(baseUrl + '/customer?nocache=' + nocache, {}, {
		query : {
			method : 'GET',
			isArray : false
		},
		update : {
			method : 'PUT',
			isArray : false
		}
	});
});

easyLaw.factory('Quote', function($resource) {
	var nocache = new Date().getTime();
	return $resource(baseUrl + '/customer/quotes/:quoteId?nocache=' + nocache,
			{}, {
				query : {
					method : 'GET',
					isArray : false
				}
			});
});

easyLaw.factory('CustomerFile', function($resource) {
	var nocache = new Date().getTime();
	return $resource(baseUrl + '/customer/file/quote/:quoteId?nocache='
			+ nocache, {}, {
		query : {
			method : 'GET',
			isArray : false
		}
	});
});

isMobile = function() {
	if ($('#some-element').css('display') == 'none')
		return true;
	else
		return false;
};