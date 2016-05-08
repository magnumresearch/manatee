(function() {
    'use strict';

    angular
        .module('manateeApp')
        .controller('ReferralSourceDeleteController',ReferralSourceDeleteController);

    ReferralSourceDeleteController.$inject = ['$uibModalInstance', 'entity', 'ReferralSource'];

    function ReferralSourceDeleteController($uibModalInstance, entity, ReferralSource) {
        var vm = this;
        vm.referralSource = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            ReferralSource.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
