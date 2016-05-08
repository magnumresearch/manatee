(function() {
    'use strict';

    angular
        .module('manateeApp')
        .controller('ReferralSourceDetailController', ReferralSourceDetailController);

    ReferralSourceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ReferralSource'];

    function ReferralSourceDetailController($scope, $rootScope, $stateParams, entity, ReferralSource) {
        var vm = this;
        vm.referralSource = entity;
        
        var unsubscribe = $rootScope.$on('manateeApp:referralSourceUpdate', function(event, result) {
            vm.referralSource = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
