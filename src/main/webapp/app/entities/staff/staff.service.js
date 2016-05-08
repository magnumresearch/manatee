(function() {
    'use strict';
    angular
        .module('manateeApp')
        .factory('Staff', Staff);

    Staff.$inject = ['$resource'];

    function Staff ($resource) {
        var resourceUrl =  'api/staff/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
