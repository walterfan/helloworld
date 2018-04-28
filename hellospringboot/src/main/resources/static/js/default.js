function output(divId, inp) {
    document.getElementById(divId).appendChild(document.createElement('pre')).innerHTML = inp;
}

function syntaxHighlight(json) {
    if (typeof json != 'string') {
        json = JSON.stringify(json, undefined, 2);
    }
    json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
        var cls = 'number';
        if (/^"/.test(match)) {
            if (/:$/.test(match)) {
                cls = 'key';
            } else {
                cls = 'string';
            }
        } else if (/true|false/.test(match)) {
            cls = 'boolean';
        } else if (/null/.test(match)) {
            cls = 'null';
        }
        return '<span class="' + cls + '">' + match + '</span>';
    });
}



(function () {
    var introService = function () {
        var _getIntro = function () {
            return {
                title: "Diagnostics based key path, event and metrics",
                lead: " Pull and Push 3K for easy diagnostics"
            }
        };
        return {
            getIntro: _getIntro
        }
    };
    var introModule = angular.module("demoApp", []);

    introModule.service('introService', introService)
        .controller('IntroController', ['introService', function (introService) {
            var vm = this;
            vm.intro = introService.getIntro();
        }]);


    var blogModule = angular.module("blogModule", []);
    blogModule.controller("blogController", function($scope, $http, $log, $q) {
        $scope.articles;
        $scope.article= {};

        $scope.query_article = function() {

            $log.info("query_article");
            $http.get("/blog/api/v1/articles")
                .then(function(response) {
                    $scope.articles = response.data;

                    $log.info("articles" + $scope.articles);
                });
            $('.tab-articles').show();
            $('.form-article').hide();
        };

        $scope.add_article = function() {
            $log.info("add_article" );
            $('.tab-articles').hide();
            $('.form-article').show();
        };

        $scope.edit_article = function(article) {
            $log.info("edit_article: " + article);
            $scope.article = article;
            $('.tab-articles').hide();
            $('.form-article').show();
        };

        $scope.delete_article = function(articleId) {
            $log.info("delete_article: " + articleId);
            $http.delete("/blog/api/v1/articles/" + articleId)
                .then(function(response) {
                    $log.info("deleted article: " + articleId);
                    $scope.query_article();
                });


        };

        $scope.update_article = function(article) {
            $log.info("update_article: " + articleId);
            var delay = $q.defer();
            $http({
                method:"PUT",
                url: "/blog/api/v1/articles/" + article.id,
                data: JSON.stringify(article)
            }) .success(function(data, status, headers, config){
                delay.resolve(data);
            })
                .error(function(data, status, headers, config){
                    delay.reject(data);
                });
            return delay.promise;
        };

        $scope.create_article = function(article) {
            $log.info("save_article: " + article);

            var delay = $q.defer();
            $http.post("/blog/api/v1/articles", JSON.stringify(article), {})
                .success(function(data, status, headers, config){
                    delay.resolve(data);
                })
                .error(function(data, status, headers, config){
                    delay.reject(data);
                });
            return delay.promise;
        };

        $scope.save_article = function() {
            $log.info("save_article: " + $scope.article);

            if($scope.article) {
                create_article($scope.article);
            } else {
                create_article($scope.article);
            }
        };
    });




})();