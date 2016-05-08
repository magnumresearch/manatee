(function() {
    'use strict';

    angular
        .module('manateeApp')
        .controller('TeamDeleteController',TeamDeleteController);

    TeamDeleteController.$inject = ['$uibModalInstance', 'entity', 'Team'];

    function TeamDeleteController($uibModalInstance, entity, Team) {
        var vm = this;
        vm.team = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Team.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
