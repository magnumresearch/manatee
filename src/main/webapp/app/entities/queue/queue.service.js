(function() {
    'use strict';
    angular
        .module('manateeApp')
        .factory('Queue', Queue);

    Queue.$inject = ['$resource', 'DateUtils'];

    function Queue ($resource, DateUtils) {
        var resourceUrl =  'api/queues/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.timestampInitial = DateUtils.convertDateTimeFromServer(data.timestampInitial);
                    data.timestampFinal = DateUtils.convertDateTimeFromServer(data.timestampFinal);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
