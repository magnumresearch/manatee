(function() {
    'use strict';
    angular
        .module('manateeApp')
        .factory('ReferralSource', ReferralSource);

    ReferralSource.$inject = ['$resource'];

    function ReferralSource ($resource) {
        var resourceUrl =  'api/referral-sources/:id';

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
