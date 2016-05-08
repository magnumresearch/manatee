(function() {
    'use strict';
    angular
        .module('manateeApp')
        .factory('Patient', Patient);

    Patient.$inject = ['$resource', 'DateUtils'];

    function Patient ($resource, DateUtils) {
        var resourceUrl =  'api/patients/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.deadline = DateUtils.convertDateTimeFromServer(data.deadline);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
